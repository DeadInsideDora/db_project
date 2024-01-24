package zxc.kyoto.util;

import zxc.kyoto.client.Client;
import java.io.IOException;

public class ClientService {
    private static Client client = new Client();

    public static String handle(Object o) throws IOException {
        client.startConnection("127.0.0.1", 4444);
        return client.sendObject(o);
    }

}
