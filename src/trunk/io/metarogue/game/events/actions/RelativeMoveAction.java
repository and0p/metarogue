package io.metarogue.game.events.actions;

import io.metarogue.client.view.threed.Vector3d;
import io.metarogue.game.gameobjects.GameObject;
import io.metarogue.game.gameobjects.Unit;

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
        }
    }

}