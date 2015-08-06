package io.metarogue.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import io.metarogue.Main;
import io.metarogue.server.user.User;
import io.metarogue.server.user.UserConnection;
import io.metarogue.util.Log;
import io.metarogue.util.network.Network;
import io.metarogue.util.messagesystem.message.Message;
import io.metarogue.util.messagesystem.message.MessageImpl;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ServerNetwork extends Server {

    int tcpport = 54555;
    int udpport = 53777;

    public ServerNetwork(int tcpport, int udpport) {
        // Register Kryo classes, same method for client and server for consistency
        Network.register(this);

        // Add object listeners
        addListener(new Listener() {
            public void received(Connection c, Object object) {
                // Cast connection to our custom connection wrapper class
                UserConnection uc = (UserConnection) c;
                // Listener for all Message objects
                if (object instanceof MessageImpl) {
                    // Cast to our custom type
                    MessageImpl message = (MessageImpl) object;
                    // Set sender based on connection ID
                    message.setSender(uc.getID());
                    // Debug, say we got a message
                    Log.log("Message for " + object.getClass().toString() + " recieved");
                    // If successful, store it to act on later
                    Main.getServer().addRemoteMessage(message);
                }
            }
        });
    }

    protected Connection newConnection () {
        // Store our own connection data on connection
        UserConnection c = new UserConnection();
        User u = new User(c);
        c.setUser(u);
        Main.getServer().addConnectingUser(u);
        return c;
    }

    public void sendAll(ConcurrentHashMap<Integer, User> players) {
        for(User u : players.values()) {
            for(Message m : u.getConnection().getMessageQueue()) {
                Log.log("Sending message " + m.getClass().toString());
                if(m.isTCP()) {
                    sendToTCP(u.getID(), m);
                } else {
                    sendToUDP(u.getID(), m);
                }
            }
            u.getConnection().clearMessages();
        }
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