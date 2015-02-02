package io.metarogue.game.events.Animation;

import org.lwjgl.util.vector.Vector3f;

public interface Animatable {

    Vector3f getDisplayPosition();
    void setDisplayPosition(Vector3f position);

}