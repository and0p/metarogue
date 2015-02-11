package io.metarogue.game.events.Animation;

import org.lwjgl.util.vector.Vector3f;

public class Animation {

    // Speed in milliseconds
    int speed = 200;
    // Rotation in full degrees
    int rotation;

    public Animation() {
    }

    public Animation(int speed) {
        this.speed = speed;
    }

    public void display(Animatable object, Vector3f startingPosition, Vector3f endingPosition, float progress) {
        if (progress <= 0) {
            object.setDisplayPosition(startingPosition);
        } else if (progress >= 1) {
            object.setDisplayPosition(endingPosition);
        } else {
            Vector3f positionDelta = new Vector3f();
            Vector3f.sub(startingPosition, endingPosition, positionDelta);
            // LWJGL Vector3f doesn't have an easy division thing that I can understand so here we goLOL
            float x = (positionDelta.getX() * progress) + startingPosition.getX();
            float y = (positionDelta.getY() * progress) + startingPosition.getY();
            float z = (positionDelta.getZ() * progress) + startingPosition.getZ();
            object.setDisplayPosition(new Vector3f(x,y,z));
        }
    }

    // Check time delta, update display position accordingly
    public void update(long currentTime) {
        // Get difference between given system time and start
        //currentTime -= startTime;
        // Divide length of animation in nanoseconds by that amount of time to get a percentage between 0&1f
        //float progress = (float)currentTime/(float)timeDelta;
        // Check to see if this value is <=0 or >=1 in which case logic is simple, otherwise update position

    }

    // Finish animation by moving to end position
    public void finish(Animatable object) {
        //object.setDisplayPosition(endingPosition);
    }

}