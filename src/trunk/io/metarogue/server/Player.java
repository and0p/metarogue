package io.metarogue.server;

import com.esotericsoftware.kryonet.Connection;
import io.metarogue.game.gameobjects.GameObject;
import io.metarogue.util.Timer;
import io.metarogue.util.network.NetworkStats;
import io.metarogue.util.network.message.NetworkMessage;
import org.lwjgl.Sys;

import java.util.ArrayList;
import java.util.Date;

public class Player extends Connection {

    int gameID;     // ID number, possibly different from
    String name;
    long timeOfConnection;

    public boolean gameSent = false;

    ArrayList<NetworkMessage> messageQueue;
    NetworkStats networkStats;

    // If player is registered with the server with a proper user/pass
    boolean registered = false;

    int timeConnected;

    // Number of possibly exploitive messages?
    int badMessages = 0;

    ArrayList<GameObject> playerOwnedObjects;
    ArrayList<GameObject> playerControlledObjects;

    public Player() {
        timeOfConnection = Timer.getMilliTime();
        networkStats = new NetworkStats();
        messageQueue = new ArrayList<NetworkMessage>();
    }

    public void addMessage(NetworkMessage m) {
        messageQueue.add(m);
    }

    public void setID(int id) {
        this.gameID = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<NetworkMessage> getMessageQueue() {
        return messageQueue;
    }

    public void clearMessages() {
        messageQueue.clear();
    }

}