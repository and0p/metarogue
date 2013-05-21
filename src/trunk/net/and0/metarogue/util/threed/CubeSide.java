package net.and0.metarogue.util.threed;

import org.lwjgl.util.vector.*;

public class CubeSide {
	// Right now I have this poorly-named class that I'm using to build meshes for some crazy reason. Please ignore me.
	
	public Vector3f corners[] = new Vector3f[4];
	public Vector3f normal = new Vector3f();
	public Vector2f textureCoord[] = new Vector2f[4];
	float halfsize;

	public CubeSide(Vector3f pos, float size, int direction) {
		halfsize = size * 0.5f;
		for(int i = 0; i < corners.length; i++) corners[i] = new Vector3f();
		for(int i = 0; i < corners.length; i++) textureCoord[i] = new Vector2f();
		// clockwise from top left, 0-3
		// directions still x+-y+-y+-
		if(direction == 0) { //x+
			textureCoord[0].set(0, .25f);
			textureCoord[1].set(.25f, .25f);
			textureCoord[2].set(.25f, .5f);
			textureCoord[3].set(0, .5f);
			normal.set(1,0,0);
			corners[0].set(pos.x + halfsize, pos.y + halfsize, pos.z + halfsize);
			corners[1].set(pos.x + halfsize, pos.y + halfsize, pos.z - halfsize);
			corners[2].set(pos.x + halfsize, pos.y - halfsize, pos.z - halfsize);
			corners[3].set(pos.x + halfsize, pos.y - halfsize, pos.z + halfsize);
		}
		if(direction == 1) { //x-
			textureCoord[0].set(0, .25f);
			textureCoord[1].set(.25f, .25f);
			textureCoord[2].set(.25f, .5f);
			textureCoord[3].set(0, .5f);
			normal.set(-1,0,0);
			corners[0].set(pos.x - halfsize, pos.y + halfsize, pos.z - halfsize);
			corners[1].set(pos.x - halfsize, pos.y + halfsize, pos.z + halfsize);
			corners[2].set(pos.x - halfsize, pos.y - halfsize, pos.z + halfsize);
			corners[3].set(pos.x - halfsize, pos.y - halfsize, pos.z - halfsize);
		}
		if(direction == 2) { //y+
			textureCoord[0].set(0, 0f);
			textureCoord[1].set(.25f, 0);
			textureCoord[2].set(.25f, .25f);
			textureCoord[3].set(0, .25f);
			normal.set(0,1,0);
			corners[0].set(pos.x - halfsize, pos.y + halfsize, pos.z - halfsize);
			corners[1].set(pos.x + halfsize, pos.y + halfsize, pos.z - halfsize);
			corners[2].set(pos.x + halfsize, pos.y + halfsize, pos.z + halfsize);
			corners[3].set(pos.x - halfsize, pos.y + halfsize, pos.z + halfsize);
		}
		if(direction == 3) {  //y-
			textureCoord[0].set(0, .5f);
			textureCoord[1].set(.25f, .5f);
			textureCoord[2].set(.25f, .75f);
			textureCoord[3].set(0, .75f);
			normal.set(0,-1,0);
			corners[0].set(pos.x - halfsize, pos.y - halfsize, pos.z + halfsize);
			corners[1].set(pos.x + halfsize, pos.y - halfsize, pos.z + halfsize);
			corners[2].set(pos.x + halfsize, pos.y - halfsize, pos.z - halfsize);
			corners[3].set(pos.x - halfsize, pos.y - halfsize, pos.z - halfsize);
		}
		if(direction == 4) { //y+
			textureCoord[0].set(0, .25f);
			textureCoord[1].set(.25f, .25f);
			textureCoord[2].set(.25f, .5f);
			textureCoord[3].set(0, .5f);
			normal.set(0,0,1);
			corners[0].set(pos.x - halfsize, pos.y + halfsize, pos.z + halfsize);
			corners[1].set(pos.x + halfsize, pos.y + halfsize, pos.z + halfsize);
			corners[2].set(pos.x + halfsize, pos.y - halfsize, pos.z + halfsize);
			corners[3].set(pos.x - halfsize, pos.y - halfsize, pos.z + halfsize);
		}
		if(direction == 5) { //y-
			textureCoord[0].set(0, .25f);
			textureCoord[1].set(.25f, .25f);
			textureCoord[2].set(.25f, .5f);
			textureCoord[3].set(0, .5f);
			normal.set(0,0,-1);
			corners[0].set(pos.x + halfsize, pos.y + halfsize, pos.z - halfsize);
			corners[1].set(pos.x - halfsize, pos.y + halfsize, pos.z - halfsize);
			corners[2].set(pos.x - halfsize, pos.y - halfsize, pos.z - halfsize);
			corners[3].set(pos.x + halfsize, pos.y - halfsize, pos.z - halfsize);
		}
	}

}