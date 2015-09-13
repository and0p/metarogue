package io.metarogue.game.timeline.actions;

import io.metarogue.util.math.Vector3d;
import io.metarogue.game.timeline.animation.Animation;
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

    // Null animations
    public void setAnimation(Animation animation) { }
    public Animation getAnimation() { return null; }

}
