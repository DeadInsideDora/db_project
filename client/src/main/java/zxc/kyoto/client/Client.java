package zxc.kyoto.client;

import zxc.kyoto.entity.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    private Socket clientSocket;
    private ObjectOutputStream out;
    private BufferedReader in;
    private User user;


    public void startConnection(String ip, int port, User user) throws IOException {
        clientSocket = new Socket(ip, port);
        System.out.println("connect");
        out = new ObjectOutputStream(clientSocket.getOutputStream());
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.user = user;
    }

    public String sendObject(Object obj) throws IOException {
        out.writeObject(obj);
        String resp = in.readLine();
        return resp;
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }
}
