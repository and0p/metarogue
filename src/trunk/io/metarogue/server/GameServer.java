package io.metarogue.server;

import io.metarogue.Main;
import io.metarogue.client.view.threed.Vector3d;
import io.metarogue.game.Game;
import io.metarogue.game.Side;
import io.metarogue.game.gameobjects.GameObject;
import io.metarogue.util.Log;
import io.metarogue.util.Timer;
import io.metarogue.util.network.message.skeleton.GameSkeleton;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class GameServer {

    ServerNetwork network;
    boolean local = true;
    boolean LAN = false;
    Game game;

    // Int for number of connected player. Used to hand out IDs
    int numOfTotalPlayers = 0;
    HashMap<Integer, Player> connectedPlayers;

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
        for(Player p : connectedPlayers.values()) {
            if(p.gameSent == false && Timer.getDeltaToNow(p.timeOfConnection) >= 1000) {
                GameSkeleton s = game.getSkeleton();
                p.messageQueue.add(s);
                p.gameSent = true;
                Log.log("Sending game!");
            }
        }
        if(!local) {
            //Parse events and put into lists to send to each particular user
            network.sendAll(connectedPlayers);
        }
    }

    public void tempInit() {
        connectedPlayers = new HashMap<Integer, Player>();
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

    public void addPlayer(Player p) {
        numOfTotalPlayers++;
        p.setID(numOfTotalPlayers);
        p.setName("Connected");
        connectedPlayers.put(numOfTotalPlayers, p);
    }

    public HashMap<Integer, Player> getPlayers() {
        return connectedPlayers;
    }

    public void loadLocalTextures() {
        game.setWorldTexture(new File("C:/metarogue/world.png"));
        game.setGUITextureFile(new File("C:/metarogue/font.png"));
    }

}