package io.metarogue.game.scope;

import io.metarogue.util.math.MortonCurve;
import io.metarogue.util.math.Rectangle;
import io.metarogue.util.math.Vector2d;

import java.util.HashSet;

public class ScopeSquare {

    Rectangle rectangle;

    public ScopeSquare(int x, int z, int size) {
        int halfSize = (int)Math.ceil(size/2);
        rectangle = new Rectangle(x - halfSize, z - halfSize, size, size);
    }

    public ScopeSquare(Vector2d v, int size) {
        int halfSize = (int)Math.ceil(size/2);
        rectangle = new Rectangle(v.getX() - halfSize, v.getZ() - halfSize, size, size);
    }

    public HashSet<Integer> getSubtraction(ScopeSquare s) {
        HashSet<Integer> hs = new HashSet<Integer>();
        // Iterate over current square, add x&z coordinates to hashset after applying morton curve
        for(int x = rectangle.getLeft(); x <= rectangle.getRight(); x++) {
            for(int z = rectangle.getTop(); z <= rectangle.getBottom(); z++) {
                if(!s.rectangle.contains(x, z)) {
                    hs.add(MortonCurve.getMorton(x,z));
                }
            }
        }
        return hs;
    }

}
