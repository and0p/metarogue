package io.metarogue.game.gamemessage.gameobject;

import io.metarogue.Main;
import io.metarogue.game.gamemessage.Intention;
import io.metarogue.game.gamemessage.world.BlockChangeMessage;
import io.metarogue.game.timeline.animation.Animatable;
import io.metarogue.util.math.Vector3d;
import io.metarogue.game.timeline.animation.Animation;
import io.metarogue.game.gameobjects.GameObject;
import io.metarogue.util.Log;
import io.metarogue.game.gamemessage.GameMessage;
import io.metarogue.util.math.Vector4d;
import org.lwjgl.util.vector.Vector3f;

public class RelativeMoveMessage extends GameMessage implements Animatable, GameObjectMessage, Intention {

    Vector3d amount;
    Animation animation;
    int gameObjectID;

    public RelativeMoveMessage(int gameObjectID, Vector3d amount) {
        this.gameObjectID = gameObjectID;
        this.amount = amount;
        animation = Main.getGame().getDefaultAnimation();
    }

    public RelativeMoveMessage(int gameObjectID, int x, int y, int z) {
        this.gameObjectID = gameObjectID;
        this.amount = new Vector3d(x,y,z);
        animation = Main.getGame().getDefaultAnimation();
    }

    public void run() {
        //
    }

    public void reverse() {
        //
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
    }

    public Animation getAnimation() {
        return animation;
    }

    public void updateAnimation(float progress) {
        //TODO: Optimize, don't need to be initializing a billion new objects
        GameObject go = Main.getGame().getGameObject(gameObjectID);
        Vector3f newPos = go.getPosition3d().toFloat();
        Vector3f originalPos = new Vector3f(newPos.getX() - amount.getX(), newPos.getY() - amount.getY(), newPos.getZ() - amount.getZ());
        animation.display(go, originalPos, newPos, progress);
    }

    public void finishAnimation() {
        GameObject go = Main.getGame().getGameObject(gameObjectID);
        animation.finish(go, go.getPosition3d().toFloat());
    }

    public boolean isTCP() {
        return true;
    }

    public int getGameObjectID() { return gameObjectID; }

    public AbsoluteMoveMessage complete() {
        GameObject go = Main.getGame().getGameObject(gameObjectID);
        if(go != null) {
            Vector3d newPosition3d = go.getPosition3d().copy();
            newPosition3d.move(amount);
            Vector4d newPosition = new Vector4d(go.getWorld(),newPosition3d);
            return new AbsoluteMoveMessage(gameObjectID, go.getPosition(), newPosition);
        } else {
            return null;
        }
    }

}