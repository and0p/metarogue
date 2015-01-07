package io.metarogue.game.events.actions;

import io.metarogue.client.view.threed.Vector3d;
import io.metarogue.game.gameobjects.Unit;

public class RelativeMoveAction extends Action {

    Unit unit;
    Vector3d amount;

    public RelativeMoveAction(Unit unit, Vector3d amount) {
        this.unit = unit;
        this.amount = amount;
    }

    public void run() {
        if(unit != null) {
            unit.move(amount);
        }
    }

}
