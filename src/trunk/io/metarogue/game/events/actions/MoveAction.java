package io.metarogue.game.events.actions;

import io.metarogue.client.view.threed.Vector3d;
import io.metarogue.game.gameobjects.Unit;

public class MoveAction extends Action {

    Unit unit;
    Vector3d position;

    public MoveAction(Unit unit, Vector3d position) {
        this.unit = unit;
        this.position = position;
    }

    public void run() {
        if(unit != null) {
            unit.setPosition(position);
        }
    }

    public void reverse() {

    }

}
