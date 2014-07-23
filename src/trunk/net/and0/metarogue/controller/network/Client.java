package net.and0.metarogue.controller.network;

import java.io.IOException;

public class Client {

    com.esotericsoftware.kryonet.Client client;

    public Client() {
        client = new com.esotericsoftware.kryonet.Client();
        client.start();
    }

    public void connect(String ip, int port) {
        try {
            client.connect(port, ip, 54555, 54777);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class SomeRequest {
        public String text;
    }
    public class SomeResponse {
        public String text;
    }

}
