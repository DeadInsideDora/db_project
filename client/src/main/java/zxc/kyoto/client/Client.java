package zxc.kyoto.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    private Socket clientSocket;
    private ObjectOutputStream out;
    private BufferedReader in;


    public void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        out = new ObjectOutputStream(clientSocket.getOutputStream());
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public String sendObject(Object obj) throws IOException {
        out.writeObject(obj);
        StringBuilder content = new StringBuilder();
        String line;

        while ((line = in.readLine()) != null) {
            content.append(line);
            content.append(System.lineSeparator());
        }

        String resp = content.toString().substring(0,content.length()-1);
        return resp;
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }
}
