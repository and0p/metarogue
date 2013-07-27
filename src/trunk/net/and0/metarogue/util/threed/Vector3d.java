package net.and0.metarogue.util.threed;

import net.and0.metarogue.model.gameworld.World;

public class Vector3d {

	int x, y, z;
	
	public Vector3d() {
		x = 0; y = 0; z = 0;
	}

    public Vector3d(int xpos, int ypos, int zpos) {
        x = xpos; y = ypos; z = zpos;
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

    public Vector3d toChunkSpace() {
        Vector3d chunked = new Vector3d(((int) Math.floor(x / 16)), ((int) Math.floor(y / 16)), ((int) Math.floor(z / 16)));
        return chunked;
    }

    public static boolean isDifferentChunkArray(Vector3d a, Vector3d b) {
        a = a.toChunkSpace();
        b = b.toChunkSpace();
        if(a.getX() != b.getX() || a.getZ() != b.getZ()) {
            return true;
        }
        return false;
    }

    public static Vector3d getDelta(Vector3d a, Vector3d b) {
        return new Vector3d(b.getX() - a.getX(), b.getY() - a.getY(), b.getZ() - a.getZ());
    }

}
