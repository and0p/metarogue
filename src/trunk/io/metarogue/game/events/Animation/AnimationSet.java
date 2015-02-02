package io.metarogue.game.events.Animation;


import io.metarogue.util.Timer;

import java.util.ArrayList;

public class AnimationSet {

    ArrayList<Animation> animations;

    public AnimationSet() {
        animations = new ArrayList<Animation>();
    }

    public void add(Animation a) {
        animations.add(a);
        a.start(Timer.getNanoTime());
    }

    public void update() {
        long nanoTime = Timer.getNanoTime();
        for(Animation a : animations) {
            a.update(nanoTime);
            if(a.isFinished()) {
                //TODO: This is a inefficient way to remove from ArrayList, should make iterator
                //animations.remove(a);
            }
        }
    }

    public void finish() {
        for(Animation a : animations) {
            a.finish();
        }
    }

}