package io.metarogue.client.view.threed;

import org.lwjgl.util.vector.Vector3f;

public class Cube {
	
	Vector3f pos;	// Center x,y,y vectors.
	int size;		// Size (since it's cube i'm assuming it's same WxDxH

	public Cube(Vector3f pos, int size) {
		this.pos = pos;
		this.size = size;
		
	}
	
	public float getSize() {
		return size;
	}
	
	public Vector3f getPos() {
		return pos;
	}

}
