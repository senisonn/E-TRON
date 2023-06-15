package fr.ptut.etron;

import fr.ptut.etron.websocket.Server;
import fr.ptut.etron.map.*;
import org.apache.log4j.BasicConfigurator;

import java.net.InetSocketAddress;

public class App {
    private static final String HOST = "192.168.0.2";
    public static String getHOST() {
        return HOST;
    }
    private static final int PORT = 8887;
    public static int getPort() {
        return PORT;
    }

    private static GameManager gameManager;
    public static GameManager getGameManager() {
        return gameManager;
    }
    private static Window window;
    public static Window getGame() {
        return window;
    }
    private static Server server;
    public static Server getServer() {
        return server;
    }

    public static void main(String[] args) {
        Map map = new Map();
        gameManager = new GameManager(map);

        window = new Window(map);
        window.start();

        // WebSocket Server
        server = new Server(new InetSocketAddress(HOST, PORT));
        
        BasicConfigurator.configure();
        
        Thread commandThread = new Thread(new CommandThread(server));
        commandThread.start();
        
        server.setReuseAddr(true);
        server.setTcpNoDelay(true);
        server.run();
    }
}
