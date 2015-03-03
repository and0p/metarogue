package io.metarogue.game.events.actions;

import io.metarogue.Main;
import io.metarogue.game.events.Animation.Animation;
import io.metarogue.client.view.threed.Vector3d;
import io.metarogue.game.gameobjects.GameObject;
import io.metarogue.util.Log;
import org.lwjgl.util.vector.Vector3f;

public class RelativeMoveAction extends Action {

    GameObject go;
    Vector3d amount;
    int x,y,z;

    public RelativeMoveAction(GameObject go, Vector3d amount) {
        this.go = go;
        this.amount = amount;
    }

    public RelativeMoveAction(GameObject go, int x, int y, int z) {
        this.go = go;
        this.amount = new Vector3d(x,y,z);
    }

    public void run() {
        if(go != null) {
            go.move(amount);
            go.setDisplayPosition(new Vector3f(go.getPosition().getX(), go.getPosition().getY(), go.getPosition().getZ()));
        }
        if(Log.logging) {
            Log.log("      Relative move action on " + go.getType() + " by " + amount.toString() + " to " + go.getPosition().toString());
        }
    }

    public void reverse() {
        if(go != null) {
            go.move(amount.reverse());
            go.setDisplayPosition(new Vector3f(go.getPosition().getX(), go.getPosition().getY(), go.getPosition().getZ()));
        }
        if(Log.logging) {
            Log.log("      Reversing relative move action on " + go.getType() + " by " + amount.toString() + " to " + go.getPosition().toString());
        }
    }

}