package io.metarogue.client;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import io.metarogue.util.Log;
import io.metarogue.util.network.Network;
import io.metarogue.util.network.NetworkStats;
import io.metarogue.util.network.message.NetworkMessage;
import io.metarogue.util.network.message.NetworkMessageImpl;

import java.io.IOException;
import java.util.ArrayList;

public class ClientNetwork extends Client {

    String serverIP;
    String localIP;
    int tcpport;
    int udpport;

    NetworkStats networkStats;

    boolean connected = false;
    boolean ready = false;

    ArrayList<NetworkMessage> messageQueue;

    public ClientNetwork(int tcpport, int udpport) {
        networkStats = new NetworkStats();
        messageQueue = new ArrayList<NetworkMessage>();
        setIdleThreshold(0);
        start();

        // Register Kryo classes, same method for client and server for consistency
        Network.register(this);

        // Add object listeners
        addListener(new Listener() {
            public void received(Connection c, Object object) {
                if (object instanceof NetworkMessageImpl) {
                    // Cast to our custom type
                    NetworkMessageImpl message = (NetworkMessageImpl) object;
                    // Debug, say we got a message
                    Log.log("Message for " + object.getClass().toString() + " recieved");
                    // Sanitize
                    message.sanitize();
                    message.run();
                    }
                }
        });
    }

    public void connect(String ip, int tcpport, int udpport) {
        try {
            super.connect(5000, ip, tcpport, udpport);
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        if(!ready && connected) {
            // send ready packet
            ready = true;
        }
        sendAll();
    }

    public void addMessage(NetworkMessage m) {
        messageQueue.add(m);
    }

    public void sendAll() {
        for(NetworkMessage m : messageQueue) {
            sendTCP(m);
        }
        messageQueue.clear();
    }

    public void message(NetworkMessage m){
        if(connected) {
            sendTCP(m);
        }
    }

    public NetworkStats getNetworkstats() {
        return networkStats;
    }

}