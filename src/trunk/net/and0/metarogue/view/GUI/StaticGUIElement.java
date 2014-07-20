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

    public StaticGUIElement copy() {
        StaticGUIElement copy = new StaticGUIElement();
        copy.id = id;
        copy.obj = obj;
        copy.variable = variable;
        copy.gameVariable = gameVariable;
        copy.displayObject = displayObject;
        copy.margin = margin;
        copy.padding = padding;
        copy.width = width;
        copy.height = height;
        copy.cornertype = cornertype;
        copy.orientation = orientation;
        copy.borderColor = borderColor;
        copy.textColor = textColor;
        copy.fillColor = fillColor;
        copy.backColor = backColor;
        copy.wrap = wrap;
        copy.absolutePosition = absolutePosition;
        copy.position = position;
        copy.tempBox = tempBox;
        copy.text = text;
        copy.fontSize = fontSize;
        copy.active = active;
        copy.visible = visible;
        copy.depth = depth;
        copy.place = place;
        copy.halign = halign;
        copy.valign = valign;
        copy.positiontype = positiontype;
        return copy;
    }

}
