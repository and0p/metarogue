package io.metarogue.game.gamemessage.gameobject;

import io.metarogue.util.math.Vector3d;
import io.metarogue.game.gameobjects.GameObject;
import io.metarogue.util.messagesystem.message.MessageImpl;

public class MoveGameObjectRelative extends MessageImpl {

    GameObject go;
    Vector3d moveAmount;

    public void run() {
        go.move(moveAmount);
    }

    public boolean isTCP() {
        return true;
    }

}