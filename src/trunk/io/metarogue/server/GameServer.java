package io.metarogue.server;

import io.metarogue.Main;
import io.metarogue.client.view.threed.Vector3d;
import io.metarogue.game.Game;
import io.metarogue.game.gameobjects.GameObject;

public class GameServer {

    ServerNetwork network;
    boolean local = true;
    boolean LAN = false;
    Game game;

    public GameServer(String gamename) {
        game = new Game(gamename);
        Main.setGame(game);
        tempInit();
    }

    public void goOnline() {
        network = new ServerNetwork(0,0);
        network.start();
        local = false;
    }

    public void update() {
        //Update game
        if(local) {
            Main.getGame().update();
        }
        if(!local) {
            //Parse events and put into lists to send to each particular user
        }
    }

    public void tempInit() {
        game.newWorld();
        GameObject player = new GameObject(new Vector3d(0,0,0), "Soldier");
        game.getDefaultWorld().addPlayerObject(player);
        game.defaultPlayer = player;
        //game.getDefaultWorld().addObject(new GameObject(new Vector3d(20, 10, 20), "Box"));
        //WorldManager.updateChunks(game.getDefaultWorld());
        game.loadLocalTextures();
    }

}