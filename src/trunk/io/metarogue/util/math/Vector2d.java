package io.metarogue.util.math;

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

    // Convert from 3d, stripping Y out, assuming this is from top-down perspective
    public Vector2d(Vector3d v) {
        x = v.getX();
        y = v.getZ();
    }
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

    // For simplicity, the "Y" can be cast to Z as well.
    public int getZ() {
        return y;
    }
	
	public void setX(int newx) {
		x = newx;
	}

	public void setY(int newz) {
		y = newz;
	}

    public void setYInverse(int newy, int inverseof) {
        y = inverseof - newy;
    }
	
	public void set( int newx, int newz) {
		x = newx;
		y = newz;
	}

}