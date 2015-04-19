package io.metarogue.client.view.threed;

import org.lwjgl.util.vector.Vector3f;

/**
 * MetaRogue class
 * User: andrew
 * Date: 5/18/13
 * time: 2:03 AM
 */
public class Ray {

    public Vector3f origin;
    public Vector3f target;

    public Vector3f normal;

    public Ray(Vector3f origin, Vector3f target) {
        this.origin = origin;
        this.target = target;
        normal = new Vector3f();

        Vector3f.sub(target, origin, normal);
        normal.normalise(normal);
    }

    public static Vector3f positionAlongRay(Ray r, Float distance) {
        return new Vector3f(r.origin.x + (r.normal.x*distance), r.origin.y + (r.normal.y*distance), r.origin.z + (r.normal.z*distance));
    }

}