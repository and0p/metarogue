package io.metarogue.game.timeline.animation;

import org.lwjgl.util.vector.Vector3f;

public interface Displayable {

    Vector3f getDisplayPosition();
    void setDisplayPosition(Vector3f position);

}