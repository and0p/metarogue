package io.metarogue.util;

import io.metarogue.client.view.threed.Vector2d;
import io.metarogue.client.view.threed.Vector3d;

// A lot of this taken right from http://code.google.com/p/treemappa/source/browse/trunk/src/org/gicentre/treemappa/MortonList.java

public class MortonCurve {

	public MortonCurve() {
		// TODO Auto-generated constructor stub
	}
	
    public static int getMorton(int x, int y)
    {
            int newX = x;
            int newY = y;
            int morton = 0;
           
            for (int i=0; i<16; i++)
            {
                    int mask = 1 << (i);
                    morton += (newY&mask)<<(i+1);
                    morton += (newX&mask)<<i;
            }      
            return morton;
    }

    public static int getX(int mortonNumber) {
        int x=0;

        for (int i=1; i<32; i+=2)
        {
            int mask = 1 << (i);
            x += (mortonNumber&mask)>>((i+1)/2);
        }

        return x;
    }

    public static int getY(int mortonNumber) {
        int y=0;

        for (int i=0; i<32; i+=2) {
            int mask = 1 << (i);
            y += (mortonNumber&mask)>>(i/2);
        }

        return y;
    }

    public static Vector2d getCoordinates(int mortonNumber) {

        int x = 0, y = 0;
        int mortonY = mortonNumber;

        for (int i=1; i<32; i+=2)
        {
            int mask = 1 << (i);
            x += (mortonNumber&mask)>>((i+1)/2);
        }

        for (int i=0; i<32; i+=2) {
            int mask = 1 << (i);
            y += (mortonY&mask)>>(i/2);
        }

        return new Vector2d(x, y);
    }

    public static int getWorldMorton(Vector3d v3d, int worldheight) {
        // Get the morton of the X & Z
        int xz = getMorton(v3d.getX(), v3d.getZ());
        // Multiply that by the world height
        xz *= worldheight;
        // Add the y and return
        return xz + v3d.getY();
    }

}