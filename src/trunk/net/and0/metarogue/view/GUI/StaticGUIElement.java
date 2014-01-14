package net.and0.metarogue.view.GUI;

import net.and0.metarogue.util.threed.Vector2d;

/**
 * MetaRogue class
 * User: andrew
 * Date: 1/11/14
 * Time: 8:42 PM
 */
public class StaticGUIElement extends GUIElement {

    public StaticGUIElement() {
        position = new Vector2d(0, 0);
    }

    public StaticGUIElement(int x, int y, int width, int height, int borderSize) {
        position = new Vector2d(x, y);
        this.width = width;
        this.height = height;
        this.borderSize[0] = borderSize;
        this.borderSize[1] = borderSize;
        this.borderSize[2] = borderSize;
        this.borderSize[3] = borderSize;
    }

    public StaticGUIElement(int x, int y, int width, int height, int borderSize, boolean absolutePosition) {
        position = new Vector2d(x, y);
        this.width = width;
        this.height = height;
        this.borderSize[0] = borderSize;
        this.borderSize[1] = borderSize;
        this.borderSize[2] = borderSize;
        this.borderSize[3] = borderSize;
        this.absolutePosition = absolutePosition;
    }

}
