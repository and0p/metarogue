package io.metarogue.game.timeline.animation;

public interface Animatable {

    Animation getAnimation();
    void updateAnimation(float progress);
    void finishAnimation();

}
