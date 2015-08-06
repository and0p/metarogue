package io.metarogue.game.timeline.actions;

import io.metarogue.game.timeline.Update;
import io.metarogue.game.timeline.animation.Animation;
import io.metarogue.game.timeline.StoryComponent;
import io.metarogue.util.Log;

public abstract class Action implements StoryComponent, Update {

    int ID;

    public Action() {
        // Auto-generated constructor
    }

    public abstract void run();

    public abstract void reverse();

    public void log() {
        Log.log("      Running that action where I was too lazy to overwrite the log method with useful text.");
    }

    public abstract void setAnimation(Animation animation);

    public abstract Animation getAnimation();

    public void updateAnimation(float progress) { }
    public void finishAnimation() { }
    public void revertAnimation() { }


}