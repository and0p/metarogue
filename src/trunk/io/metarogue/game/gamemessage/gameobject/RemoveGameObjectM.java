package io.metarogue.game.gamemessage.gameobject;

import io.metarogue.Main;
import io.metarogue.game.gamemessage.GameMessage;
import io.metarogue.game.gameobjects.GameObject;

public class RemoveGameObjectM extends GameMessage {

    int gameObjectID;
    transient GameObject gameObject;

    // Empty constructor for Kryonet
    public RemoveGameObjectM() {}

    public RemoveGameObjectM(int gameObjectID) {
        this.gameObjectID = gameObjectID;
    }

    public int getGameObjectID() {
        return gameObjectID;
    }

    public void run() {
        makeReversible();
        Main.getGame().removeGameObject(gameObjectID);
    }

    public void reverse() {
        Main.getGame().addGameObject(gameObject);
    }

    public void makeReversible() {
        gameObject = Main.getGame().getGameObject(gameObjectID);
    }

}
