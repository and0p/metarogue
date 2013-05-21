package net.and0.metarogue.util.threed;

public class Vector2d {

	int x, y;
	
	public Vector2d(int xpos, int zpos) {
		x = xpos;
		y = zpos;
	}
	
	public Vector2d(Vector2d v) {
		x = v.getX();
		y = v.getY();
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public void setX(int newx) {
		x = newx;
	}

	public void setY(int newz) {
		y = newz;
	}
	
	public void set( int newx, int newz) {
		x = newx;
		y = newz;
	}

}