package io.metarogue.server.user;

import com.esotericsoftware.kryonet.Connection;
import io.metarogue.game.gameobjects.GameObject;
import io.metarogue.util.Timer;
import io.metarogue.util.network.NetworkStats;
import io.metarogue.util.network.message.NetworkMessage;
import org.lwjgl.Sys;

import java.util.ArrayList;
import java.util.Date;

public class UserConnection extends Connection {

    User user;
    ArrayList<NetworkMessage> messageQueue;

    // Number of possibly exploitive messages?
    int badMessages = 0;

    public UserConnection() {
        messageQueue = new ArrayList<NetworkMessage>();
    }

    public void addMessage(NetworkMessage m) {
        messageQueue.add(m);
    }

    public ArrayList<NetworkMessage> getMessageQueue() {
        return messageQueue;
    }

    public void clearMessages() {
        messageQueue.clear();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}