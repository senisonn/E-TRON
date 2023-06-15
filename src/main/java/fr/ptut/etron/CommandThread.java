package fr.ptut.etron;

import org.java_websocket.server.WebSocketServer;

import java.util.Scanner;

public class CommandThread implements Runnable {

    private final WebSocketServer server;

    public CommandThread(WebSocketServer server) {
        this.server = server;
    }

    public void run() {
        Scanner scan = new Scanner(System.in);
        while (true) {
            String command = scan.nextLine();

            if (command.equalsIgnoreCase("stop")) {
                try {
                    server.stop();
                    System.out.println("Server stopped");
                    Thread.sleep(500); // wait for the server to stop
                    System.exit(0); // exit the programs
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else if (command.equalsIgnoreCase("start")) {
                App.getGameManager().start();
            }
        }
    }
}
