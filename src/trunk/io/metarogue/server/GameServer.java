package io.metarogue.server;

import io.metarogue.Main;
import io.metarogue.client.view.threed.Vector3d;
import io.metarogue.game.Game;
import io.metarogue.game.Side;
import io.metarogue.game.gameobjects.GameObject;
import io.metarogue.server.user.User;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

public class GameServer {

    ServerNetwork network;
    boolean local = true;
    boolean LAN = false;
    Game game;

    ConcurrentHashMap<Integer, User> users;

    public GameServer(String gamename) {
        game = new Game(gamename);
        Main.setGame(game);
        game.init();
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
            //Parse events and put into lists to send to each particular user
            network.sendAll(users);
        }
    }

    public void tempInit() {
        users = new ConcurrentHashMap<Integer, User>();
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

    public void addUser(User u) {
        users.put(u.getConnection().getID(), u);
    }

    public User getUser(int i) {
        return users.get(i);
    }

    public ConcurrentHashMap<Integer, User> getUsers() {
        return users;
    }

    public void loadLocalTextures() {
        game.setWorldTexture(new File("C:/metarogue/world.png"));
        game.setGUITextureFile(new File("C:/metarogue/font.png"));
    }

}