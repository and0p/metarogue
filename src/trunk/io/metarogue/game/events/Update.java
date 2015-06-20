package io.metarogue.game.events;

public interface Update {

    public void updateAnimation(float progress);
    //public void updateAnimation(int progress);
    public void finishAnimation();
    public void revertAnimation();

    public void run();
    public void reverse();

}