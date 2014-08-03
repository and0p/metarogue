package io.metarogue.client.view.threed;

import org.lwjgl.util.vector.Vector3f;

public class Triangle {
	
	public Vector3f vertices[] = new Vector3f[3];

	public Triangle(Vector3f one, Vector3f two, Vector3f three) {
		vertices[0] = one;
		vertices[1] = two;
		vertices[2] = three;
	}
	
	public Triangle() {
		vertices[0].set(0,0,0);
		vertices[1].set(0,0,0);
		vertices[2].set(0,0,0);
	}
	
	public Triangle(float x1, float y1, float z1,
					float x2, float y2, float z2,
					float x3, float y3, float z3) {
		vertices[0].set(x1,y1,z1);
		vertices[1].set(x2,y2,z2);
		vertices[2].set(x3,y3,z3);
	}
	
	public void setVertex(int vertex, float x, float y, float z) {
		vertices[vertex].set(x,y,z);
	}

}