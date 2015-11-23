package io.metarogue.game.gamemessage.gameobject;

import io.metarogue.Main;
import io.metarogue.game.Game;
import io.metarogue.game.gamemessage.GameMessage;
import io.metarogue.util.math.Vector3d;
import io.metarogue.game.gameobjects.GameObject;
import io.metarogue.util.messagesystem.message.MessageImpl;

public class MoveGameObjectRelative extends GameMessage implements GameObjectMessage {

    int gameObjectID;
    transient Vector3d originalPosition;
    transient Vector3d newPosition;
    Vector3d moveAmount;
    int world;
    boolean run = false;

    public MoveGameObjectRelative() {}

    public MoveGameObjectRelative(GameObject go, Vector3d moveAmount) {
        gameObjectID = go.getID();
        this.moveAmount = moveAmount;
    }

    public void run() {
        GameObject go = Main.getGame().getGameObject(gameObjectID);
        if(!run) {
            world = go.getWorld();
            originalPosition = go.getPosition3d().copy();
            go.move(moveAmount);
            newPosition = go.getPosition3d().copy();
        } else {
            go.move(moveAmount);
        }
    }

    public void reverse() {
        Main.getGame().getGameObject(gameObjectID).move(moveAmount.reverse());
    }

    public boolean isTCP() {
        return true;
    }

    public Vector3d getMoveAmount() {
        return moveAmount;
    }

    public void setMoveAmount(Vector3d moveAmount) {
        this.moveAmount = moveAmount;
    }

    public int getGameObjectID() {
        return gameObjectID;
    }

    public void setGameObjectID(int gameObjectID) {
        this.gameObjectID = gameObjectID;
    }

    public Vector3d getOriginalPosition() {
        return originalPosition;
    }

    public Vector3d getNewPosition() {
        return newPosition;
    }

    public int getWorld() {
        return world;
    }

}