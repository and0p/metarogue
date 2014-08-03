package io.metarogue.client.view.threed;

import org.lwjgl.util.vector.*;

public class CubeSide {
	
	public Vector3f corners[] = new Vector3f[4];
	public Vector3f normal = new Vector3f();
	public Vector2f textureCoord[];
    static float textureFloat = 0.083333333333f;
	static float halfsize;
    int side = 0;

	public CubeSide(Vector3f pos, float size, int direction, int type) {
		halfsize = size * 0.5f;
		for(int i = 0; i < corners.length; i++) corners[i] = new Vector3f();
        side = direction;
		// clockwise from top left, 0-3
		// directions still x+-y+-z+-
		if(direction == 0) { //x+
            textureCoord = WorldTextureBuilder.getTextureCoordinates(type, 1);
			normal.set(1,0,0);
			corners[0].set(pos.x + halfsize, pos.y + halfsize, pos.z + halfsize);
			corners[1].set(pos.x + halfsize, pos.y + halfsize, pos.z - halfsize);
			corners[2].set(pos.x + halfsize, pos.y - halfsize, pos.z - halfsize);
			corners[3].set(pos.x + halfsize, pos.y - halfsize, pos.z + halfsize);
		}
		if(direction == 1) { //x-
            textureCoord = WorldTextureBuilder.getTextureCoordinates(type, 1);
			normal.set(-1,0,0);
			corners[0].set(pos.x - halfsize, pos.y + halfsize, pos.z - halfsize);
			corners[1].set(pos.x - halfsize, pos.y + halfsize, pos.z + halfsize);
			corners[2].set(pos.x - halfsize, pos.y - halfsize, pos.z + halfsize);
			corners[3].set(pos.x - halfsize, pos.y - halfsize, pos.z - halfsize);
		}
		if(direction == 2) { //y+
            textureCoord = WorldTextureBuilder.getTextureCoordinates(type, 0);
			normal.set(0,1,0);
			corners[0].set(pos.x - halfsize, pos.y + halfsize, pos.z - halfsize);
			corners[1].set(pos.x + halfsize, pos.y + halfsize, pos.z - halfsize);
			corners[2].set(pos.x + halfsize, pos.y + halfsize, pos.z + halfsize);
			corners[3].set(pos.x - halfsize, pos.y + halfsize, pos.z + halfsize);
		}
		if(direction == 3) {  //y-
            textureCoord = WorldTextureBuilder.getTextureCoordinates(type, 2);
			normal.set(0,-1,0);
			corners[0].set(pos.x - halfsize, pos.y - halfsize, pos.z + halfsize);
			corners[1].set(pos.x + halfsize, pos.y - halfsize, pos.z + halfsize);
			corners[2].set(pos.x + halfsize, pos.y - halfsize, pos.z - halfsize);
			corners[3].set(pos.x - halfsize, pos.y - halfsize, pos.z - halfsize);
		}
		if(direction == 4) { //z+
            textureCoord = WorldTextureBuilder.getTextureCoordinates(type, 1);
			normal.set(0,0,1);
			corners[0].set(pos.x - halfsize, pos.y + halfsize, pos.z + halfsize);
			corners[1].set(pos.x + halfsize, pos.y + halfsize, pos.z + halfsize);
			corners[2].set(pos.x + halfsize, pos.y - halfsize, pos.z + halfsize);
			corners[3].set(pos.x - halfsize, pos.y - halfsize, pos.z + halfsize);
		}
		if(direction == 5) { //z-
            textureCoord = WorldTextureBuilder.getTextureCoordinates(type, 1);
			normal.set(0,0,-1);
			corners[0].set(pos.x + halfsize, pos.y + halfsize, pos.z - halfsize);
			corners[1].set(pos.x - halfsize, pos.y + halfsize, pos.z - halfsize);
			corners[2].set(pos.x - halfsize, pos.y - halfsize, pos.z - halfsize);
			corners[3].set(pos.x + halfsize, pos.y - halfsize, pos.z - halfsize);
		}
	}

}