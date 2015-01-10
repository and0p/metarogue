package io.metarogue.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import io.metarogue.util.Log;
import io.metarogue.util.network.Network;
import io.metarogue.util.network.message.NetworkMessageImpl;

import java.io.IOException;
import java.util.ArrayList;

public class ServerNetwork extends Server {

    int tcpport = 54555;
    int udpport = 54777;

    ArrayList<PlayerConnection> connectedPlayers;
    ArrayList<PlayerConnection> connectingPlayers;

    public ServerNetwork(int tcpport, int udpport) {
        // Register Kryo classes, same method for client and server for consistency
        Network.register(this);

        // Add object listeners
        addListener(new Listener() {
            public void received(Connection c, Object object) {
                // Cast to our custom connection wrapper class
                PlayerConnection connection = (PlayerConnection)c;

//                if (object instanceof ConnectionMessage) {
//                    ConnectionMessage message = (ConnectionMessage)object;
//                    System.out.println("Connection recieved");
//                    message.verify();
//                    message.run();
//
//                    TextMessage response = new TextMessage();
//                    response.text = "Thanks";
//                    connection.sendTCP(response);
//                }

                if (object instanceof NetworkMessageImpl) {
                    // Cast to our custom type
                    NetworkMessageImpl message = (NetworkMessageImpl)object;
                    // Debug, say we got a message
                    Log.log("Message for " + object.getClass().toString() + " recieved");
                    message.verify();
                    message.run(); // TODO: make threadsafe
                }

            }
        });
    }

    protected Connection newConnection () {
        // Store our own connection data on connection
        return new PlayerConnection();
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