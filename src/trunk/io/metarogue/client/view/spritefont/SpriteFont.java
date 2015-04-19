package io.metarogue.client.view.spritefont;

import org.newdawn.slick.opengl.Texture;

/**
 * MetaRogue class
 * User: andrew
 * Date: 6/4/13
 * time: 12:50 AM
 */
public class SpriteFont {

    Texture spritesheet;
    int tilewidth;
    int tileheight;

    public SpriteFont() {
        // Auto-generated constructor
    }

    public SpriteFont(Texture texture, int tilewidth, int tileheight) {
        spritesheet = texture;
        this.tilewidth = tilewidth;
        this.tileheight = tileheight;
    }

    public void positionThingy(byte b) {

    }

    public float[] draw(){
        return null;
    }

}
