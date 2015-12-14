package io.metarogue.game.gamemessage.gameobject;

import io.metarogue.Main;
import io.metarogue.game.gamemessage.GameMessage;
import io.metarogue.game.gameobjects.GameObject;

public class AddGameObjectM extends GameMessage {

    GameObject gameObject;

    // Empty constructor for Kryonet
    public AddGameObjectM() {}

    public AddGameObjectM(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    public GameObject getGameObject() {
        return gameObject;
    }

    public void run() {
        Main.getGame().addGameObject(gameObject);
    }

    public void reverse() {
        Main.getGame().removeGameObject(gameObject.getID());
    }

}