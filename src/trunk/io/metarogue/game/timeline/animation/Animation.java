package io.metarogue.game.timeline.animation;

import org.lwjgl.util.vector.Vector3f;

public class Animation {

    // Speed in milliseconds
    int duration = 200;
    // Rotation in full degrees
    int rotation;

    public Animation() {
    }

    public Animation(int speed) {
        this.duration = speed;
    }

    // Set display position of object passed based on animation progress
    public void display(Animatable object, Vector3f startingPosition, Vector3f endingPosition, float progress) {
        if (progress <= 0) {
            object.setDisplayPosition(startingPosition);
        } else if (progress >= 1) {
            object.setDisplayPosition(endingPosition);
        } else {
            Vector3f positionDelta = new Vector3f(endingPosition.getX() - startingPosition.getX(), endingPosition.getY() - startingPosition.getY(), endingPosition.getZ() - startingPosition.getZ());
            // LWJGL Vector3f doesn't have an easy division thing that I can understand so here we goLOL
            float x = (positionDelta.getX() * progress) + startingPosition.getX();
            float y = (positionDelta.getY() * progress) + startingPosition.getY();
            float z = (positionDelta.getZ() * progress) + startingPosition.getZ();
            object.setDisplayPosition(new Vector3f(x,y,z));
        }
    }


    public void display(Animatable object, Vector3f startingPosition, Vector3f endingPosition, int millseconds) {
        display(object, startingPosition, endingPosition, getProgressAfterMilliseconds(millseconds));
    }

    // Function for finding how far along an animation would be (float 0-1) after m amount of milliseconds
    public float getProgressAfterMilliseconds(long m) {
        if(m == 0 || duration == 0) return 1;
        return (float)m / duration;
    }

    public float getProgressAfterAdditionalMilliseconds(float progress, long milliseconds) {
        int passedTime = (int)(duration * progress);
        return getProgressAfterMilliseconds(passedTime + milliseconds);
    }

    public long getMillisecondsFromProgress(float p) {
        if(p >= 1) {
            return duration;
        } else if (p <= 0) {
            return 0;
        } else {
            return (long)(duration * p);
        }
    }

    // Finish animation by moving to end position
    public void finish(Animatable object, Vector3f endingPosition) {
        object.setDisplayPosition(endingPosition);
    }

    public int getDuration() {
        return duration;
    }

}