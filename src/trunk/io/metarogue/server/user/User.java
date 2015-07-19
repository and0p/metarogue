package io.metarogue.server.user;

// TODO: This will eventually represent a registered "user" for the server for persistence

import io.metarogue.Main;
import io.metarogue.game.Player;

public class User {

    int id;
    String username;
    boolean registered;

    // Player object from game model
    Player player;
    // Network connection in Kryo
    UserConnection connection;

    long timeOfConnection;

    public User(UserConnection connection) {
        this.connection = connection;
        id = connection.getID();
    }

    public void register(String username) {
        this.username = username;
    }

    public Player createPlayer() {
        Player p = new Player(id, username);
        Main.getGame().getPlayers().put(id, p);
        return p;
    }

    // Getters and setters:

    public UserConnection getConnection() {
        return connection;
    }

    public void setConnection(UserConnection connection) {
        this.connection = connection;
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

}