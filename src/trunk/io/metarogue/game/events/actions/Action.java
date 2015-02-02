package io.metarogue.game.events.actions;

import io.metarogue.game.events.Animation.Animation;
import io.metarogue.util.Log;

import java.util.logging.Logger;

public abstract class Action {

    int ID;
    Animation animation;

    public Action() {
        // Auto-generated constructor
    }

    public abstract void run();

    public void log() {
        Log.log("      Running that action where I was too lazy to overwrite the log method with useful text.");
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
    }

    public Animation getAnimation() {
        return animation;
    }

}