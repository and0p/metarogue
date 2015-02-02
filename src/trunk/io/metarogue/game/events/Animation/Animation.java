package io.metarogue.game.events.Animation;

import io.metarogue.util.Timer;
import org.lwjgl.util.vector.Vector3f;

public class Animation {

    Animatable object;

    // Start and end points in animation
    Vector3f startingPosition;
    Vector3f endingPosition;
    // Difference between the two
    Vector3f positionDelta = new Vector3f();

    // Speed in milliseconds
    int speed = 200;
    // Rotation in full degrees
    int rotation = 0;

    // Start and end time in nanoseconds.
    long startTime;
    long endTime;
    // Difference between the two
    long timeDelta;

    boolean finished = false;

    public Animation(Animatable object, Vector3f endingPosition) {
        // If no starting point set, assume old display position
        startingPosition = object.getDisplayPosition();
        this.endingPosition = endingPosition;
        // Set positionDelta
        Vector3f.sub(endingPosition, startingPosition, positionDelta);
    }

    public Animation(Animatable object, Vector3f startingPosition, Vector3f endingPosition) {
        this.object = object;
        this.startingPosition = startingPosition;
        this.endingPosition = endingPosition;
        // Set positionDelta
        Vector3f.sub(endingPosition, startingPosition, positionDelta);
    }

    // Start with given time
    public void start(long time) {
        startTime = time;
        endTime = time + Timer.convertMillisecondsToNanoseconds(speed);
        timeDelta = endTime - startTime;
    }

    // Check time delta, update display position accordingly
    public void update(long currentTime) {
        // Get difference between given system time and start
        currentTime -= startTime;
        // Divide length of animation in nanoseconds by that amount of time to get a percentage between 0&1f
        float progress = (float)currentTime/(float)timeDelta;
        // Check to see if this value is <=0 or >=1 in which case logic is simple, otherwise update position
        if (progress <= 0) {
            object.setDisplayPosition(startingPosition);
        } else if (progress >= 1) {
            object.setDisplayPosition(endingPosition);
            finished = true;
        } else {
            // LWJGL Vector3f doesn't have an easy division thing that I can understand so here we goLOL
            float x = (positionDelta.getX() * progress) + startingPosition.getX();
            float y = (positionDelta.getY() * progress) + startingPosition.getY();
            float z = (positionDelta.getZ() * progress) + startingPosition.getZ();
            object.setDisplayPosition(new Vector3f(x,y,z));
        }

    }

    // Finish animation by moving to end position
    public void finish() {
        object.setDisplayPosition(endingPosition);
        finished = true;
    }

    public boolean isFinished() {
        return finished;
    }

}