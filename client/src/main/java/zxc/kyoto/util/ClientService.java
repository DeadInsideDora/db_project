package zxc.kyoto.util;

import zxc.kyoto.client.Client;

import java.io.IOException;

public class ClientService {
    private static Client client = new Client();

    public static String service(Object o) {
        try {
            client.startConnection("127.0.0.1", 4444);
            System.out.println("ping");
            return client.sendObject(o);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
