/* Containers */
const connectionLostContainer = document.getElementById('connectionLostContainer');
const waitingToConnectContainer = document.getElementById('waitingToConnectContainer');
const waitingGameStartContainer = document.getElementById('waitingGameStartContainer');
const gameIsFullContainer = document.getElementById('gameIsFullContainer');
const gameAlreadyStartedContainer = document.getElementById('gameAlreadyStartedContainer');
const gameContainer = document.getElementById('gameContainer');
const deathContainer = document.getElementById('deathContainer');
const wonContainer = document.getElementById('wonContainer');

var displayedContainer = null;

function setDisplayedContainer(containerToDisplay) {
    if (displayedContainer != null)
        displayedContainer.style.display = 'none';

    displayedContainer = containerToDisplay;
    displayedContainer.style.display = 'block';
}

function updateWaitingGameStartContainer() {
    const waitingGameStartScreenUsername = document.getElementById('waitingGameStartScreenUsername');
    const waitingGameStartScreenOnlinePlayers = document.getElementById('waitingGameStartScreenOnlinePlayers');

    waitingGameStartScreenUsername.innerHTML = "Pseudonyme : " + playerUsername;
    waitingGameStartScreenOnlinePlayers.innerHTML = onlinePlayers + " / " + maxPlayers + " joueurs connectés";
    waitingGameStartContainer.style.backgroundColor = playerColor;
}

function updateGameContainer() {
    const gameScreenUsername = document.getElementById('gameScreenUsername');
    const gameScreenLives = document.getElementById('gameScreenLives');
    const gameScreenControlLeft = document.getElementById('gameScreenControlLeft');
    const gameScreenControlRight = document.getElementById('gameScreenControlRight');

    gameScreenUsername.innerHTML = playerUsername;
    gameScreenLives.innerHTML = "Vies restantes : " + playerLives;
    gameScreenControlLeft.style.backgroundColor = playerColor;
    gameScreenControlRight.style.backgroundColor = playerColor;
}

function updateDeathContainer() {
    const deathScreenRank = document.getElementById('deathScreenRank');

    deathScreenRank.innerHTML = "Classement : " + playerRank + " / " + onlinePlayers;
}

/* Containers */

/* Game variables */
var onlinePlayers = 0;
var maxPlayers = 50;

var playerUsername = "username";
var playerColor = "#000000";
var playerLives = 3;
var playerRank = 0;

var socket = null;
/* Game variables */

/* Socket */
function connect() {
    console.log("Connecting to server...");
    socket = new WebSocket("ws://192.168.1.15:8887");

    socket.onopen = (event) => {
        console.log("Connection successful");
    };

    socket.onclose = (event) => {
        if (displayedContainer == waitingToConnectContainer) {
            console.log("Connection failed, retrying in 1 second");
            setTimeout(connect, 1000);
            return;
        } else {
            console.log("Connection closed");
            socket.send("Deconnexion");
            setDisplayedContainer(connectionLostContainer);
        }
    };

    socket.onmessage = (event) => {
        if (displayedContainer == waitingToConnectContainer) {
            if (event.data == "gameIsFull") {
                setDisplayedContainer(gameIsFullContainer);
            } else if (event.data == "gameAlreadyStarted") {
                setDisplayedContainer(gameAlreadyStartedContainer);
            } else if (event.data.startsWith("connectionSuccessful")) {
                const splitted = event.data.split(":");

                playerUsername = splitted[1];
                playerColor = splitted[2];

                updateWaitingGameStartContainer();
                setDisplayedContainer(waitingGameStartContainer);
            }
        } else if (displayedContainer == waitingGameStartContainer) {
            if (event.data.startsWith("onlinePlayers")) {
                const splitted = event.data.split(":");
                onlinePlayers = parseInt(splitted[1]);
                maxPlayers = parseInt(splitted[2]);

                updateWaitingGameStartContainer();
            } else if (event.data.startsWith("gameStarted")) {
                updateGameContainer();
                setDisplayedContainer(gameContainer);
            }
        } else if (displayedContainer == gameContainer) {
            if (event.data.startsWith("lives")) {
                const splitted = event.data.split(":");
                playerLives = parseInt(splitted[1]);

                if (playerLives == 0) {
                    playerRank = parseInt(splitted[2]);

                    updateDeathContainer();
                    setDisplayedContainer(deathContainer);
                } else {
                    updateGameContainer();
                }
            } else if (event.data.startsWith("gameWon")) {
                setDisplayedContainer(wonContainer);
            }
        }
    };
}

function sendLeft() {
    if (displayedContainer != gameContainer)
        return;

    socket.send("l");
}

function sendRight() {
    if (displayedContainer != gameContainer)
        return;

    socket.send("r");
}

/* Socket */


/* Utils */
function reload() {
    location.reload();
}

document.addEventListener("keydown", function (event) {
    if (event.key === "ArrowLeft") {
        sendLeft();
    }
    if (event.key === "ArrowRight") {
        sendRight();
    }
});
/* Utils */

setDisplayedContainer(waitingToConnectContainer);
connect();
