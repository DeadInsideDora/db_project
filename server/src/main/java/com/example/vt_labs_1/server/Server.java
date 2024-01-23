package com.example.vt_labs_1.server;

import com.example.vt_labs_1.utility.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;

public class Server {

    private int port;
    private ServerSocket server;

    public Server() throws IOException {
        this.port = 4444;
        boolean connect = false;
        while (!connect) {
            try {
                server = new ServerSocket(port);
                connect = true;
                System.out.println("Сервер поднят и доступен по порту " + port + " .");
            } catch (Exception e) {
                port = (int) (Math.random() * 20000 + 10000);
            }
        }
        Config config = new Config(Paths.get("config.properties"));
        DataBaseHandler dataBaseHandler = new DataBaseHandler(config);
        while (true) {
            new ClientHandler(server.accept()).start();
        }
    }

    public void stop() throws IOException {
        server.close();
    }

    private static class ClientHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter out;
        private ObjectInputStream in;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new ObjectInputStream(clientSocket.getInputStream());
                Object obj = in.readObject();
                //obrabotka
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }


            out.println("answer");

            try {
                in.close();
                clientSocket.close();
                out.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
