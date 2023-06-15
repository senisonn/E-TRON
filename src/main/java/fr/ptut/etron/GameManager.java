package fr.ptut.etron;

import fr.ptut.etron.player.Player;
import fr.ptut.etron.utils.Config;
import fr.ptut.etron.utils.Direction;
import fr.ptut.etron.utils.Vector2;
import fr.ptut.etron.websocket.Server;
import fr.ptut.etron.bonus.*;
import fr.ptut.etron.map.*;
import org.java_websocket.WebSocket;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class GameManager {

    private Map<WebSocket, Player> playerMap;
    private GameState gameState;
    public fr.ptut.etron.map.Map map;
    public LinkedList<Bonus> bonusList; 

    public GameManager(fr.ptut.etron.map.Map map) {
        this.gameState = GameState.WAITING;
        this.playerMap = new HashMap<>();
        this.map = map;
        this.bonusList = new LinkedList<Bonus>();
        computeStartPositions();
    }

    public void start() {
        this.gameState = GameState.STARTED;

        App.getServer().broadcast("gameStarted");
        System.out.println("Partie lancée");
    }

    public void playerJoinEvent(Server server, WebSocket webSocket) {
        if (playerMap.size() < Config.MAX_PLAYERS) {
            final Player player = new Player(this, webSocket, this.map);
            playerMap.put(webSocket, player);
            webSocket.send("connectionSuccessful:" + player.getName() + ":#" + Integer.toHexString(player.getColor().getRGB()).substring(2));
            server.broadcast("onlinePlayers:" + playerMap.size() + ":" + Config.MAX_PLAYERS);

            computeStartPositions();
        } else {
            webSocket.send("gameIsFull");
        }
    }

    public void computeStartPositions() {
        final int offset = 50;
        final Map<WebSocket, Player> playerMap = new HashMap<>(this.playerMap);
        final int onlinePlayers = playerMap.size();

        int countLeft = 0, countRight = 0, countTop = 0, countBottom = 0;
        for (int i = 0; i < onlinePlayers; i++) {
            switch (i % 4) {
                case 0:
                    countLeft++;
                    break;
                case 1:
                    countRight++;
                    break;
                case 2:
                    countTop++;
                    break;
                case 3:
                    countBottom++;
                    break;
            }
        }

        final int spaceBeetweenLeft = Config.WINDOW_HEIGHT / (countLeft + 1);
        final int spaceBeetweenRight = Config.WINDOW_HEIGHT / (countRight + 1);
        final int spaceBeetweenTop = Config.WINDOW_WIDTH / (countTop + 1);
        final int spaceBeetweenBottom = Config.WINDOW_WIDTH / (countBottom + 1);

        AtomicInteger i = new AtomicInteger(0);
        this.playerMap.forEach((s, player) -> {
            final int lap = (i.get() / 4) + 1;
            switch (i.get() % 4) {
                case 0:
                    player.setPosition(new Vector2(offset, spaceBeetweenLeft * lap));
                    player.setDirection(Direction.EAST);
                    break;
                case 1:
                    player.setPosition(new Vector2(Config.WINDOW_WIDTH - offset, spaceBeetweenRight * lap));
                    player.setDirection(Direction.WEST);
                    break;
                case 2:
                    player.setPosition(new Vector2(spaceBeetweenTop * lap, offset));
                    player.setDirection(Direction.SOUTH);
                    break;
                case 3:
                    player.setPosition(new Vector2(spaceBeetweenBottom * lap, Config.WINDOW_HEIGHT - offset));
                    player.setDirection(Direction.NORTH);
                    break;
            }
            i.getAndIncrement();
        });
    }

    public void playerDiedEvent(Player player) {
        final WebSocket webSocket = player.getWebSocket();
        if (webSocket != null) {
            playerMap.remove(webSocket);
        }

        if (App.getGameManager().getGameState() == GameState.STARTED && playerMap.size() == 1) {
            this.gameState = GameState.ENDED;

            final Player winner = playerMap.values().iterator().next();
            final WebSocket winnerWebSocket = winner.getWebSocket();
            if (winnerWebSocket != null) {
                winnerWebSocket.send("gameWon");
            }
            System.out.println("Partie terminée");
        }
    }

    public Map<WebSocket, Player> getPlayerMap() {
        return playerMap;
    }

    public GameState getGameState() {
        return gameState;
    }

    public enum GameState {
        WAITING, STARTED, ENDED;
    }
}
