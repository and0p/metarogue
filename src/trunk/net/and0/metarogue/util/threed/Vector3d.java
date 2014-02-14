package net.and0.metarogue.util.threed;

import net.and0.metarogue.model.gameworld.World;
import org.lwjgl.util.vector.Vector3f;

public class Vector3d {

	int x, y, z;
	
	public Vector3d() {
		x = 0; y = 0; z = 0;
	}

    public Vector3d(int xpos, int ypos, int zpos) {
        x = xpos; y = ypos; z = zpos;
    }

    public Vector3d(Vector3f v) {
        x = (int)v.getX(); y = (int)v.getY(); z = (int)v.getZ();
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

    public void move( int x, int y, int z) {
        this.x += x;
        this.y += y;
        this.z += z;
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

    public static boolean isDifferentChunk(Vector3d a, Vector3d b) {
        a = a.toChunkSpace();
        b = b.toChunkSpace();
        if(a.getX() != b.getX() || a.getZ() != b.getZ() || a.getY() != b.getY()) {
            return true;
        }
        return false;
    }

    public Vector3d copy() {
        return new Vector3d(x,y,z);
    }

    // Get relative position.
    public static Vector3d getDelta(Vector3d a, Vector3d b) {
        return new Vector3d(b.getX() - a.getX(), b.getY() - a.getY(), b.getZ() - a.getZ());
    }

    public Vector3f toFloat() {
        return new Vector3f(x, y, z);
    }

    public boolean equals(Vector3d c) {
        if(c.getX() == x && c.getY() == y && c.getZ() == z) return true;
        return false;
    }

    public static Vector3d zero() {
        return new Vector3d(0,0,0);
    }

}