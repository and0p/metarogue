package io.metarogue.game.timeline.actions;

import io.metarogue.Main;
import io.metarogue.client.view.threed.Vector3d;
import io.metarogue.game.timeline.animation.Animation;
import io.metarogue.game.gameobjects.GameObject;
import io.metarogue.util.Log;
import org.lwjgl.util.vector.Vector3f;

public class RelativeMoveAction extends Action {

    Vector3d amount;
    Animation animation;
    int gameObjectID;

    public RelativeMoveAction(int gameObjectID, Vector3d amount) {
        this.gameObjectID = gameObjectID;
        this.amount = amount;
        animation = Main.getGame().getDefaultAnimation();
    }

    public RelativeMoveAction(int gameObjectID, int x, int y, int z) {
        this.gameObjectID = gameObjectID;
        this.amount = new Vector3d(x,y,z);
        animation = Main.getGame().getDefaultAnimation();
    }

    public void run() {
        GameObject go = Main.getGame().getGameObject(gameObjectID);
        if(go != null) {
            go.move(amount);
            go.setDisplayPosition(new Vector3f(go.getPosition().getX(), go.getPosition().getY(), go.getPosition().getZ()));
        }
        if(Log.logging) {
            Log.log("      Relative move action on " + go.getType() + " by " + amount.toString() + " to " + go.getPosition().toString());
        }
    }

    public void reverse() {
        GameObject go = Main.getGame().getGameObject(gameObjectID);
        if(go != null) {
            go.move(amount.reverse());
            go.setDisplayPosition(new Vector3f(go.getPosition().getX(), go.getPosition().getY(), go.getPosition().getZ()));
        }
        if(Log.logging) {
            Log.log("      Reversing relative move action on " + go.getType() + " by " + amount.toString() + " to " + go.getPosition().toString());
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
        Vector3f newPos = go.getPosition().toFloat();
        Vector3f originalPos = new Vector3f(newPos.getX() - amount.getX(), newPos.getY() - amount.getY(), newPos.getZ() - amount.getZ());
        animation.display(go, originalPos, newPos, progress);
    }

    public void finishAnimation() {
        GameObject go = Main.getGame().getGameObject(gameObjectID);
        animation.finish(go, go.getPosition().toFloat());
    }

}