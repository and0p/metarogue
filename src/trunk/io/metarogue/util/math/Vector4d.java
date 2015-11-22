package io.metarogue.util.math;

import org.lwjgl.util.vector.Vector3f;

public class Vector4d {

    Vector3d v;
    public int w;

    public Vector4d() {
        w = 0;
        v = new Vector3d();
    }

    public Vector4d(int world, int xpos, int ypos, int zpos) {
        w = world;
        v = new Vector3d(xpos, ypos, zpos);
    }

    public Vector4d(int world, Vector3d v) {
        w = world;
        this.v = v;
    }

    public Vector4d(int world, Vector3f v) {
        w = world;
        this.v = new Vector3d(v);
    }

    public int getWorld() {
        return w;
    }
    public void setWorld(int w) {
        this.w = w;
    }

    public Vector3d getVector3d() { return v; }
    public void setVector3d(Vector3d v) { this.v = v; }

}