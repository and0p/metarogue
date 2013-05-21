package net.and0.metarogue.util.threed;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Face {
	
	public Vector3f vertices[];
	public Vector3f normal;
	public Vector2f textureCoord[];

	public Face(Vector3f one, Vector3f two, Vector3f three, Vector3f normal) {
		vertices = new Vector3f[3];
		this.normal = normal;
		vertices[0] = one;
		vertices[1] = two;
		vertices[2] = three;
	}
	
	public Face(Vector3f one, Vector3f two, Vector3f three, Vector2f tone, Vector2f ttwo, Vector2f tthree, Vector3f normal) {
		vertices = new Vector3f[3];
		textureCoord = new Vector2f[3];
		this.normal = normal;
		vertices[0] = one;
		vertices[1] = two;
		vertices[2] = three;
		textureCoord[0] = tone;
		textureCoord[1] = ttwo;
		textureCoord[2] = tthree;
	}
	
	public Face() {
		vertices[0].set(0,0,0);
		vertices[1].set(0,0,0);
		vertices[2].set(0,0,0);
	}
	
	public void setVertex(int vertex, float x, float y, float z) {
		vertices[vertex].set(x,y,z);
	}
}