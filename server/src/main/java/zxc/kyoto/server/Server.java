package zxc.kyoto.server;

import zxc.kyoto.dao.DataBaseService;
import zxc.kyoto.dao.UsersService;
import zxc.kyoto.entity.Request;
import zxc.kyoto.util.Config;
import zxc.kyoto.util.DataBaseHandler;
import zxc.kyoto.entity.User;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;

import static zxc.kyoto.util.Roles.ADMIN;

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
        DataBaseService.init(new DataBaseHandler(config));
        UsersService.users.put("admin", new User("admin", "admin", ADMIN));
        Module.init();
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
                Request req = (Request) in.readObject();
                out.println(new Module().handle(req));
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }



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
