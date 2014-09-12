package io.metarogue.client;

import com.esotericsoftware.kryonet.Client;
import io.metarogue.util.network.Network;
import io.metarogue.util.network.message.TextMessage;

import java.io.IOException;

public class ClientNetwork extends Client {

    String serverIP;
    String localIP;
    int tcpport;
    int udpport;

    boolean connected = false;
    boolean local = false;

    public ClientNetwork() {
        Network.register(this);
        start();
    }

    public void connect(String ip, int tcpport, int udpport) {
        try {
            super.connect(5000, ip, tcpport, udpport);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void message(){
        if(!connected) {
            TextMessage request = new TextMessage();
            request.text = "Here is the request";
            sendTCP(request);
        }
        connected = true;
    }

}