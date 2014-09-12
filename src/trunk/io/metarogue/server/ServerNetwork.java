package io.metarogue.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import io.metarogue.util.network.Network;
import io.metarogue.util.network.message.TextMessage;

import java.io.IOException;

public class ServerNetwork extends Server {

    int tcpport = 54555;
    int udpport = 54777;

    public ServerNetwork() {
        // Register Kryo classes, same method for client and server for consistency
        Network.register(this);

        // Add object listeners
        addListener(new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof TextMessage) {
                    TextMessage request = (TextMessage) object;
                    System.out.println(request.text);

                    TextMessage response = new TextMessage();
                    response.text = "Thanks";
                    connection.sendTCP(response);
                }
            }
        });
    }

    public void start() {
        try {
            bind(tcpport, udpport);
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.start();
    }
    
    public void cleanup() {

    }

}