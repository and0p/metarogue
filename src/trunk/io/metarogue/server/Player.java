package io.metarogue.server;

import com.esotericsoftware.kryonet.Connection;
import io.metarogue.game.gameobjects.GameObject;
import io.metarogue.util.network.NetworkStats;
import org.lwjgl.Sys;

import java.util.ArrayList;
import java.util.Date;

public class Player extends Connection {

    int id;
    String name;
    Date timeOfConnection;

    NetworkStats networkStats;

    // If player is registered with the server with a proper user/pass
    boolean registered = false;

    int timeConnected;

    // Number of possibly exploitive messages?
    int badMessages = 0;

    ArrayList<GameObject> playerOwnedObjects;
    ArrayList<GameObject> playerControlledObjects;

    public Player() {
        timeOfConnection = new Date();
        timeOfConnection.setTime(Sys.getTime());
    }

    public void setID(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

}