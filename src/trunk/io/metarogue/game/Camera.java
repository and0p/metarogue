package io.metarogue.game;

import io.metarogue.util.math.Vector3d;
import org.lwjgl.util.vector.Vector3f;
import java.lang.Math;

/**
* Camera that can fly or orbit target/point
*
* @author and0
*/

public class Camera {
	
	// Create modes for free flying camera and orbiting
    private static enum CameraMode {
        FREE, ORBIT
    }
    
    private static CameraMode mode;						// List of modes (free or orbit?)

    public Vector3f upVector = new Vector3f(0, 1, 0);   // Up vector

    // VARIABLES FOR CAMERA IN FREE MODE
	public Vector3f position = new Vector3f(0, 0, 0);	// 3d point in space for position
	private float yaw = 	0.0f;						// Yaw (Y axis)
	private float pitch = 	0.0f;						// Pitch (X axis)
	private float roll = 	0.0f;						// Roll (Z axis, if needed for any reason)
	
	// VARIABLES FOR CAMERA IN ORBIT MODE
	double camera_radius = 8.0;							// Dolly level, distance from target
	public Vector3f target = new Vector3f(0, 0, 0);		// Camera's orbiting target
	public float[] rot = {0,0};							// Degrees of rotation
	
	// Settings
	float minimumDistance = 0.01f;
	float maximumDistance = 5000;
	float maximumOrbitAngle = 89.999f;
	float minimumOrbitAngle = -89.999f;
	
	// Set camera rotation in orbit mode
	public void rotateCamera(float x, float y) {
		rot[0] += x;
		rot[1] += y;
		normalizeAngles();
        //setUpVector();
		position.x = (float) (target.x + camera_radius*-Math.sin(Math.toRadians(rot[0]))*Math.cos(Math.toRadians(rot[1])));
		position.y = (float) (target.y + camera_radius*                                 Math.sin(Math.toRadians(rot[1])));
		position.z = (float) (target.z + camera_radius*Math.cos(Math.toRadians(rot[0]))*Math.cos(Math.toRadians(rot[1])));
	}
	
	// "Dolly" camera by changing orbiting distance
	public void dollyCamera(double d) {
		camera_radius += d;
		if(camera_radius > maximumDistance) camera_radius = maximumDistance;
		if(camera_radius < minimumDistance) camera_radius = minimumDistance;
		rotateCamera(0,0);
	}
	
	// Once you get over 360 degrees change back to 0 + difference, and vice versa
	void normalizeAngles() {
		if(rot[0] > 360) rot[0] += -360;
		if(rot[0] < 0) 	 rot[0] += 360;
		if(rot[1] > maximumOrbitAngle)  rot[1] = maximumOrbitAngle;
		if(rot[1] < minimumOrbitAngle)  rot[1] = minimumOrbitAngle;
	}

    void setUpVector() {
        if(rot[1] == 90 || rot[1] == -90) {
            upVector.setX(0.01f);
        }
        else {
            upVector.setX(0f);
        }
    }
	
	// Constructor for free camera
	public Camera(float x, float y, float z) {
		// Set mode as "FREE"
		mode = CameraMode.FREE;
		
		// Apply xyz from creation as position
		position.x = x;
		position.y = y;
		position.z = z;
	}
	
	// Constructor for orbiting camera
	public Camera(float dist, int x, int y, int z) {
		// Set mode as "ORBIT"
		mode = CameraMode.ORBIT;
		
		// Assign target and position, also default rotation of 0,0
		target = new Vector3f(x, y, z);
		position = new Vector3f(x, y, z);
		rotateCamera(0,0);
	}

    public void setTarget(Vector3d target) {
        this.target.set(target.getX(), target.getY(), target.getZ());
    }

    public void setTarget(Vector3f target) {
        this.target = target;
    }

    public void setTargetAndUpdate(Vector3f target) {
        this.target = target;
        rotateCamera(0,0);
    }

    public Vector3d targetV3D() {
        return new Vector3d(target);
    }

    public Vector3d targetChunk() {
        Vector3d v3d = new Vector3d(target);
        return v3d.toChunkSpace();
    }

}