package io.metarogue.client;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import io.metarogue.util.Log;
import io.metarogue.util.network.Network;
import io.metarogue.util.network.NetworkStats;
import io.metarogue.util.messagesystem.message.Message;
import io.metarogue.util.messagesystem.message.MessageImpl;
import io.metarogue.util.messagesystem.message.connection.RegistrationMessage;

import java.io.IOException;
import java.util.ArrayList;

public class ClientNetwork extends Client {

    String serverIP;
    String localIP;
    int tcpport = 54555;
    int udpport = 53777;

    NetworkStats networkStats;

    public boolean connected = false;
    boolean registered = false;

    ArrayList<Message> messageQueue;

    public ClientNetwork(int tcpport, int udpport) {
        networkStats = new NetworkStats();
        messageQueue = new ArrayList<Message>();
        setIdleThreshold(0);
        start();

        // Register Kryo classes, same method for client and server for consistency
        Network.register(this);

        // Add object listeners
        addListener(new Listener() {
            public void received(Connection c, Object object) {
                if (object instanceof MessageImpl) {
                    // Cast to our custom type
                    MessageImpl message = (MessageImpl) object;
                    // Debug, say we got a message
                    Log.log("Message for " + object.getClass().toString() + " recieved");
                    // Sanitize
                    message.sanitize();
                    message.runAsClient();  //TODO: Add to pool to run when game thread is ready instead
                    }
                }
        });
    }

    // Try connecting. Return true/false based on success
    public boolean connect(String ip) {
        try {
            super.connect(5000, ip, tcpport, udpport);
            connected = true;
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Log.log("Connection failed");
            return false;
        }
    }

    public void update() {
        if(!registered && connected) {
            sendTCP(new RegistrationMessage("Default"));
            registered = true;
        }
        sendAll();
    }

    public void addMessage(Message m) {
        messageQueue.add(m);
    }

    public void sendAll() {
        for(Message m : messageQueue) {
            if(m.isTCP()) {
                sendTCP(m);
            } else {
                sendUDP(m);
            }
        }
        messageQueue.clear();
    }

    public void message(Message m){
        if(connected) {
            sendTCP(m);
        }
    }

    public NetworkStats getNetworkstats() {
        return networkStats;
    }

}