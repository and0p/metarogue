package io.metarogue.game.gamemessage.gameobject;

import io.metarogue.Main;
import io.metarogue.game.gamemessage.GameMessage;
import io.metarogue.game.gameobjects.GameObject;
import io.metarogue.game.timeline.animation.Animatable;
import io.metarogue.game.timeline.animation.Animation;
import io.metarogue.util.Log;
import io.metarogue.util.math.Vector3d;
import io.metarogue.util.math.Vector4d;
import org.lwjgl.util.vector.Vector3f;

public class AbsoluteMoveM extends GameMessage implements Animatable, GameObjectM {

    Vector4d originalPosition;
    Vector4d endingPosition;
    Animation animation;
    int gameObjectID;

    public AbsoluteMoveM(int gameObjectID, Vector4d originalPosition, Vector4d endingPosition) {
        this.gameObjectID = gameObjectID;
        this.originalPosition = originalPosition;
        this.endingPosition = endingPosition;
    }

    public void run() {
        GameObject go = Main.getGame().getGameObject(gameObjectID);
        if(go != null) {
            go.setPosition(endingPosition.getVector3d());
            go.setDisplayPosition(new Vector3f(go.getPosition3d().getX(), go.getPosition3d().getY(), go.getPosition3d().getZ()));
        }
        if(Log.logging) {
            //Log.log("      Relative setPosition action on " + gameObject.getType() + " by " + amount.toString() + " to " + gameObject.getPosition().toString());
        }
    }

    public void reverse() {
        GameObject go = Main.getGame().getGameObject(gameObjectID);
        if(go != null) {
            go.setPosition(originalPosition.getVector3d());
            go.setDisplayPosition(new Vector3f(go.getPosition3d().getX(), go.getPosition3d().getY(), go.getPosition3d().getZ()));
        }
        if(Log.logging) {
            //Log.log("      Reversing relative setPosition action on " + gameObject.getType() + " by " + amount.toString() + " to " + gameObject.getPosition().toString());
        }
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
        animation.display(go, originalPosition.getVector3d().toFloat(), endingPosition.getVector3d().toFloat(), progress);
    }

    public void finishAnimation() {
        GameObject go = Main.getGame().getGameObject(gameObjectID);
        animation.finish(go, go.getPosition3d().toFloat());
    }

    public boolean isTCP() {
        return true;
    }

    public int getGameObjectID() { return gameObjectID; }

    public boolean objectLeavesChunk() {
        return Vector3d.isDifferentChunk(originalPosition.getVector3d(), endingPosition.getVector3d());
    }

    public Vector4d getOriginalPosition() {
        return originalPosition;
    }

    public Vector4d getEndingPosition() {
        return endingPosition;
    }

}
