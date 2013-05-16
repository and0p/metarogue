package net.and0.metarogue.threed;

public class Vector3d {

	int x, y, z;
	
	public Vector3d(int xpos, int ypos, int zpos) {
		x = xpos;
		y = ypos;
		z = zpos;
	}
	
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public int getZ() {
		return z;
	}
	
	public void setX(int newx) {
		x = newx;
	}
	public void setY(int newy) {
		y = newy;
	}
	public void setZ(int newz) {
		z = newz;
	}
	
	public void set( int newx, int newy, int newz) {
		x = newx;
		y = newy; 
		z = newz;
	}


}
