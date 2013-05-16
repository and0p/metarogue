package net.and0.metarogue.threed;

public class Vector2d {

	int x, z;
	
	public Vector2d(int xpos, int zpos) {
		x = xpos;
		z = zpos;
	}
	
	public Vector2d(Vector2d v) {
		x = v.getX();
		z = v.getZ();
	}
	
	public int getX() {
		return x;
	}

	public int getZ() {
		return z;
	}
	
	public void setX(int newx) {
		x = newx;
	}

	public void setZ(int newz) {
		z = newz;
	}
	
	public void set( int newx, int newz) {
		x = newx;
		z = newz;
	}

}
