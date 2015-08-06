package io.metarogue.server;

import io.metarogue.Main;
import io.metarogue.client.view.threed.Vector3d;
import io.metarogue.game.Game;
import io.metarogue.game.Side;
import io.metarogue.game.gameobjects.GameObject;
import io.metarogue.server.user.User;
import io.metarogue.util.messagesystem.MessagePump;
import io.metarogue.util.messagesystem.message.Message;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class GameServer {

    ServerNetwork network;
    boolean local = true;
    boolean LAN = false;
    Game game;

    ConcurrentHashMap<Integer, User> users;
    ArrayList<User> connectingUsers;

    MessagePump messagePump;

    // List of messages in various states
    ArrayList<Message> unsanitizedMessages;
    ArrayList<Message> unverifiedMessages;
    ArrayList<Message> messages;

    public GameServer(String gamename) {
        game = new Game(gamename);
        Main.setGame(game);
        game.init();
        users = new ConcurrentHashMap<Integer, User>();
        connectingUsers = new ArrayList<User>();
        // Initialize message buffers
        unsanitizedMessages = new ArrayList<Message>();
        unverifiedMessages = new ArrayList<Message>();
        messages = new ArrayList<Message>();
        // Debug init...
        tempInit();
    }

    public void goOnline() {
        network = new ServerNetwork(54555,54777);
        network.start();
        local = false;
    }

    public void update() {
        //Update game
        Main.getGame().update();
        if(!local) {
            // See if users are fully connected, if so add them to main list.
            checkConnectedUsers();
            //Parse timeline and put into lists to send to each particular user
            network.sendAll(users);
        }
    }

    public void tempInit() {
        game.getSides().add(new Side(0, "Players"));
        game.getSides().add(new Side(1, "Enemies"));
        game.newWorld();
        GameObject player = new GameObject(new Vector3d(0,16,0), "Soldier");
        game.getDefaultWorld().addPlayerObject(player);
        game.defaultPlayer = player;
        game.addGameObject(player, 0);
        for(int i = 0; i < 12; i++) {
            GameObject go = new GameObject(new Vector3d((int)(Math.random()*12), 16, (int)(Math.random()*12)), "Soldier");
            game.addGameObject(go, 1);
        }
        //WorldManager.updateChunks(game.getDefaultWorld());
        loadLocalTextures();
    }

    public void runMessages() {
        boolean success;
        for(Message m : unsanitizedMessages) {
            success = m.sanitize();
            if(success) {
                unverifiedMessages.add(m);
            } else {
                //TODO: Acknowledge possibly exploitive message
            }
        }
        unsanitizedMessages.clear();
        for(Message m : unverifiedMessages) {
            success = m.verify();
            if(success) {
                messages.add(m);
            }
        }
        unverifiedMessages.clear();
        for(Message m : messages) {
            m.runAsServer();
        }
    }

    public void checkConnectedUsers() {
        if(connectingUsers.size() > 0) {
                for(Iterator<User> iterator = connectingUsers.iterator(); iterator.hasNext();) {
                    User user = iterator.next();
                    if (user != null && user.getConnection().isConnected()) {
                        // Get proper ID
                        user.setID(user.getConnection().getID());
                        // Add to real users, which creates player object and broadcasts
                        addUser(user);
                        // Remove from this list as it is fully connected
                        iterator.remove();
                    }
                }
        }
        // See if anyone has left, if so disconnect and create message for all
        for(User user : users.values()) {

        }
    }

    public void addMessage(Message m) {
        messages.add(m);
    }

    public void addRemoteMessage(Message m) {
        unsanitizedMessages.add(m);
    }

    public void sendMessage(int userId, Message m) {
        User u = getUser(userId);
        if(u != null) {
            u.addMessage(m);
        }
    }

    public void sendMessageToAll(Message m) {
        for(User u : users.values()) {
            u.addMessage(m);
        }
    }

    public void sendMessageToAllExcept(int userId, Message m) {
        for(User u : users.values()) {
            if(u.getID() != userId) {
                u.addMessage(m);
            }
        }
    }

    public void addConnectingUser(User u) {
        connectingUsers.add(u);
    }

    public void addUser(User u) {
        users.put(u.getConnection().getID(), u);
    }

    public User getUser(Integer i) {
        if(users.containsKey(i)) {
            return users.get(i);
        } else {
            return null;
        }
    }

    public ConcurrentHashMap<Integer, User> getUsers() {
        return users;
    }

    public void loadLocalTextures() {
        game.setWorldTexture(new File("C:/metarogue/world.png"));
        game.setGUITextureFile(new File("C:/metarogue/font.png"));
    }

}