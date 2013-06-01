package net.and0.metarogue.model.GUI;

import java.util.List;

import net.and0.metarogue.util.settings.DisplaySettings;
import net.and0.metarogue.util.threed.Box;
import net.and0.metarogue.util.threed.Vector2d;

public class GUIElement {

    public String id = null;                    // ID of this element
    public String obj = null;                   // Game object this element is getting values from
    public String variable = null;              // Variable it's trying to pull from game object (ie "health")

    public int[] margin = new int[]{5,5,5,5};   // Margin in pixels
    public int[] padding = new int[]{5,5,5,5};  // Internal padding in pixels
	
	public int width = 30;                      // Width in pixels
	public int height = 30;                     // Height in pixels

    public boolean absolutePosition = false;    // Position type, absolute vs relative
	
	public Vector2d position;                   // Ideal absolute / relative position, x and y in pixels
    public Box tempBox = new Box();             // Actual bounding box of element after absolute / relative placement

	public int bordersize = 10;                 // Size of border in pixels
	
	public String text = "";                    // Text to display in this element
	
	public boolean active = true;               // Whether or not element is active
	public boolean visible = true;              // Whether or not element is visible

    // Alignment horizontally
    public static enum hAlign { LEFT, RIGHT, CENTER; }
    // Alignment vertically
    public static enum vAlign { TOP, BOTTOM, CENTER; }

    public hAlign halign = hAlign.LEFT;
    public vAlign valign = vAlign.TOP;

    public GUIElement() {
        position = new Vector2d(0, 0);
    }

	public GUIElement(int x, int y, int width, int height, int bordersize) {
		position = new Vector2d(x, y);
		this.width = width;
		this.height = height;
		this.bordersize = bordersize;
	}

    public GUIElement(int x, int y, int width, int height, int bordersize, boolean absolutePosition) {
        position = new Vector2d(x, y);
        this.width = width;
        this.height = height;
        this.bordersize = bordersize;
        this.absolutePosition = absolutePosition;
    }

    public void setPosition(int x, int y) {
        position.set(x, y);
    }
    public void setX(int x) {
        position.setX(x);
    }
    public void setY(int y) {
        position.setY(y);
    }
}
