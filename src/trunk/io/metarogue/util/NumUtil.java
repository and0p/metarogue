package io.metarogue.util;

public class NumUtil {
	
    public static int unflattenArray3dBox(int x, int y, int z, int dimensions) {
        return (x + (y*dimensions) + (z*dimensions*dimensions));
    }

}