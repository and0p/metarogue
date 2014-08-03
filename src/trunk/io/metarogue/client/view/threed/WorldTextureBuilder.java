package io.metarogue.client.view.threed;

import org.lwjgl.util.vector.*;

public class WorldTextureBuilder {

    public WorldTextureBuilder() {
        // Auto-generated constructor
    }

    static float width = 256;
    static float height = 48;

    public static Vector2f[] getTextureCoordinates(int blockType, int side) {
        Vector2f[] corners = new Vector2f[4];
        for(int i = 0; i < corners.length; i++) corners[i] = new Vector2f();
        if(side == 0) {
            corners[0].setX(blockType/width);
            corners[0].setY(0);
            corners[1].setX((blockType+1)/width);
            corners[1].setY(0);
            corners[2].setX((blockType+1)/width);
            corners[2].setY(0.25f);
            corners[3].setX(blockType/width);
            corners[3].setY(0.25f);
        }
        if(side == 1) {
            corners[0].setX(blockType/width);
            corners[0].setY(0.25f);
            corners[1].setX((blockType+1)/width);
            corners[1].setY(0.25f);
            corners[2].setX((blockType+1)/width);
            corners[2].setY(0.5f);
            corners[3].setX(blockType/width);
            corners[3].setY(0.5f);
        }
        if(side == 2) {
            corners[0].setX(blockType/width);
            corners[0].setY(0.5f);
            corners[1].setX((blockType+1)/width);
            corners[1].setY(0.5f);
            corners[2].setX((blockType+1)/width);
            corners[2].setY(0.75f);
            corners[3].setX(blockType/width);
            corners[3].setY(0.75f);
        }
        return corners;
    }

}
