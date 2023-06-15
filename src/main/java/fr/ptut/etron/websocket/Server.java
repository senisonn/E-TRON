package fr.ptut.etron.websocket;

import fr.ptut.etron.App;
import fr.ptut.etron.GameManager;
import fr.ptut.etron.deprecated.*;
import fr.ptut.etron.player.Player;
import fr.ptut.etron.utils.Config;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.*;


public class Server extends WebSocketServer implements IConfig {

    private GameManager gameManager;

    public Server(InetSocketAddress address) {
        super(address);

        this.gameManager = App.getGameManager();
    }

    @Override
    public synchronized void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("new connection to " + conn.getRemoteSocketAddress().toString());
        if (this.gameManager.getGameState() != GameManager.GameState.WAITING) {
            conn.send("gameAlreadyStarted");
            return;
        }

        this.gameManager.playerJoinEvent(this, conn);
        /*final List<Player> players = new ArrayList<>(this.gameManager.getPlayerMap().values());
        for (Player player : players) {
            player.getTrail().setLength((int)(player.getTrail().getLength()/players.size()));
        }*/
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        /*System.out.println("closed " + conn.getRemoteSocketAddress().toString() + " with exit code " + code + " additional info: " + reason);*/
        final Map<WebSocket, Player> playerMap = this.gameManager.getPlayerMap();
        final Player player = playerMap.get(conn);
        if (player != null) {
            if (this.gameManager.getGameState() != GameManager.GameState.ENDED) {
                player.setDead(true);
                this.gameManager.playerDiedEvent(player);
                broadcast("onlinePlayers:" + playerMap.size() + ":" + Config.MAX_PLAYERS);
            } else {
                player.setWebSocket(null);
            }
        }
    }

    @Override
    public synchronized void onMessage(WebSocket conn, String message) {
        final Player player = this.gameManager.getPlayerMap().get(conn);
        if (player == null)
            return;
        if(message.equals("Deconnexion"))
            System.out.println("deconnexion");
        if (message.equals("l")) {
            this.gameManager.getPlayerMap().get(conn).turnLeft();
        } else if (message.equals("r")) {
            this.gameManager.getPlayerMap().get(conn).turnRight();
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.err.println("an error occurred on connection:" + ex);
    }

    @Override
    public void onStart() {
        System.out.println("server started successfully");
    }

}