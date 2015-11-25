package io.metarogue.game.scope;

import io.metarogue.Main;
import io.metarogue.util.math.MortonCurve;
import io.metarogue.util.math.Rectangle;
import io.metarogue.util.math.Vector2d;

import java.util.HashSet;

public class ScopeSquare {

    Rectangle rectangle;

    public ScopeSquare(int x, int z) {
        int scopeDiameter = (Main.getGame().getScopeDistance()*2+1);
        int halfSize = (int)Math.ceil(scopeDiameter/2);
        rectangle = new Rectangle(x - halfSize, z - halfSize, scopeDiameter, scopeDiameter);
    }

    public ScopeSquare(Vector2d v) {
        int scopeDiameter = (Main.getGame().getScopeDistance()*2+1);
        int halfSize = (int)Math.ceil(scopeDiameter/2);
        rectangle = new Rectangle(v.getX() - halfSize, v.getZ() - halfSize, scopeDiameter, scopeDiameter);
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
