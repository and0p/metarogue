package net.and0.metarogue.view.GUI;

import net.and0.metarogue.util.threed.Box;
import net.and0.metarogue.util.threed.Vector2d;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

import javax.swing.tree.DefaultMutableTreeNode;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex3f;

public class GUIElement {

    public String id = null;                    // ID of this element
    public String obj = null;                   // Game object this element is getting values from
    public String variable = null;              // Variable it's trying to pull from game object (ie "health")

    public int[] margin = new int[]{0,0,0,0};   // Margin in pixels
    public int[] padding = new int[]{5,5,5,5};  // Internal padding in pixels

    public int width = 30;                      // Width in pixels
    public int height = 30;                     // Height in pixels

    public boolean absolutePosition = false;    // Position type, absolute vs relative

    public Vector2d position;                   // Ideal absolute / relative position, x and y in pixels
    public Box tempBox = new Box();             // Actual bounding box of element after absolute / relative placement

    public int bordersize = 10;                 // Size of border in pixels

    public String text = "";                    // Text to display in this element
    public static int fontSize = 5;

    public boolean active = true;               // Whether or not element is active
    public boolean visible = true;              // Whether or not element is visible

    public int depth = 0;                       // Depth of this element in tree
    public int place = 0;                       // Place of this element amongst children. (0 would be left-most, I'd assume.)

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

    public void render() {

        int displayHeight = Display.getHeight(); //TODO make this smarter. Or something, no need to grab this every time I render A Thing.

//        GUIElement e = (GUIElement)node.getUserObject();
//        float depth = node.getLevel();


        // Establish a bounding box
        Vector2d[] corners = {  tempBox.getCorner(0), tempBox.getCorner(1), tempBox.getCorner(2), tempBox.getCorner(3)	};
        // Subtract display height from Y so it sticks to the to top instead of the bottom
        for(int i = 0; i < 4; i++) {
            corners[i].setY(displayHeight - corners[i].getY());
        }

        // Render inside
        if(bordersize > 0) {
            //Render center
            glBegin(GL_QUADS);
            glTexCoord2f(.75f,0);
            glVertex3f(corners[0].getX() + bordersize, corners[0].getY() - bordersize, 0);
            glTexCoord2f(.75f,0);
            glVertex3f(corners[1].getX() - bordersize, corners[1].getY() - bordersize, 0);
            glTexCoord2f(.75f,1);
            glVertex3f(corners[2].getX() - bordersize, corners[2].getY() + bordersize, 0);
            glTexCoord2f(.75f,1);
            glVertex3f(corners[3].getX() + bordersize, corners[3].getY() + bordersize, 0);
            glEnd();
            // Top-left corner
            glBegin(GL_QUADS);
            glTexCoord2f(0,0);
            glVertex3f(corners[0].getX(), corners[0].getY(), 0);
            glTexCoord2f(.25f,0);
            glVertex3f(corners[0].getX() + bordersize, corners[0].getY(), 0);
            glTexCoord2f(.25f,1);
            glVertex3f(corners[0].getX() + bordersize, corners[0].getY() - bordersize, 0);
            glTexCoord2f(0,1);
            glVertex3f(corners[0].getX(), corners[0].getY() -+ bordersize, 0);
            glEnd();
            // Top-right corner
            glBegin(GL_QUADS);
            glTexCoord2f(.25f,0);
            glVertex3f(corners[1].getX() - bordersize, corners[1].getY(), 0);
            glTexCoord2f(0,0);
            glVertex3f(corners[1].getX(), corners[1].getY(), 0);
            glTexCoord2f(0,1);
            glVertex3f(corners[1].getX(), corners[1].getY() - bordersize, 0);
            glTexCoord2f(.25f,1);
            glVertex3f(corners[1].getX() - bordersize, corners[1].getY() - bordersize, 0);
            glEnd();
            // Bottom-right corner
            glBegin(GL_QUADS);
            glTexCoord2f(.25f,1);
            glVertex3f(corners[2].getX() - bordersize, corners[2].getY() + bordersize, 0);
            glTexCoord2f(0,1);
            glVertex3f(corners[2].getX(), corners[2].getY() + bordersize, 0);
            glTexCoord2f(0,0);
            glVertex3f(corners[2].getX(), corners[2].getY(), 0);
            glTexCoord2f(.25f,0);
            glVertex3f(corners[2].getX() - bordersize, corners[2].getY(), 0);
            glEnd();
            // Bottom-left corner
            glBegin(GL_QUADS);
            glTexCoord2f(0,1);
            glVertex3f(corners[3].getX(), corners[3].getY() + bordersize, 0);
            glTexCoord2f(.25f,1);
            glVertex3f(corners[3].getX() + bordersize, corners[3].getY() + bordersize, 0);
            glTexCoord2f(.25f,0);
            glVertex3f(corners[3].getX() + bordersize, corners[3].getY(), 0);
            glTexCoord2f(0,0);
            glVertex3f(corners[3].getX(), corners[3].getY(), 0);
            glEnd();
            // Top side
            glBegin(GL_QUADS);
            glTexCoord2f(.25f,0);
            glVertex3f(corners[0].getX() + bordersize, corners[0].getY(), 0);
            glTexCoord2f(.5f,0);
            glVertex3f(corners[1].getX() - bordersize, corners[1].getY(), 0);
            glTexCoord2f(.5f,1);
            glVertex3f(corners[1].getX() - bordersize, corners[1].getY() - bordersize, 0);
            glTexCoord2f(.25f,1);
            glVertex3f(corners[0].getX() + bordersize, corners[0].getY() - bordersize, 0);
            glEnd();
            // Right side
            glBegin(GL_QUADS);
            glTexCoord2f(.25f,1);
            glVertex3f(corners[1].getX() - bordersize, corners[1].getY() - bordersize, 0);
            glTexCoord2f(.5f,0);
            glVertex3f(corners[1].getX(), corners[1].getY() - bordersize, 0);
            glTexCoord2f(.5f,0);
            glVertex3f(corners[2].getX(), corners[2].getY() + bordersize, 0);
            glTexCoord2f(.25f,1);
            glVertex3f(corners[2].getX() - bordersize, corners[2].getY() + bordersize, 0);
            glEnd();
            // Bottom side
            glBegin(GL_QUADS);
            glTexCoord2f(.25f,1);
            glVertex3f(corners[3].getX() + bordersize, corners[3].getY() + bordersize, 0);
            glTexCoord2f(.5f,1);
            glVertex3f(corners[2].getX() - bordersize, corners[2].getY() + bordersize, 0);
            glTexCoord2f(.5f,0);
            glVertex3f(corners[2].getX() - bordersize, corners[2].getY(), 0);
            glTexCoord2f(.25f,0);
            glVertex3f(corners[3].getX() + bordersize, corners[2].getY(), 0);
            glEnd();
            // Left side
            glBegin(GL_QUADS);
            glTexCoord2f(.5f, 0);
            glVertex3f(corners[0].getX(), corners[1].getY() - bordersize, 0);
            glTexCoord2f(.5f, 1);
            glVertex3f(corners[0].getX() + bordersize, corners[1].getY() - bordersize, 0);
            glTexCoord2f(.25f, 1);
            glVertex3f(corners[3].getX() + bordersize, corners[3].getY() + bordersize, 0);
            glTexCoord2f(.25f, 0);
            glVertex3f(corners[3].getX(), corners[3].getY() + bordersize, 0);
            glEnd();
        }
        else {
            glBegin(GL_QUADS);
            glVertex3f(corners[0].getX(), corners[0].getY(), 0);
            glVertex3f(corners[1].getX(), corners[1].getY(), 0);
            glVertex3f(corners[2].getX(), corners[2].getY(), 0);
            glVertex3f(corners[3].getX(), corners[3].getY(), 0);
            glEnd();
        }

        //renderCharacter(new Vector2d(50,50), (byte)44);


    }

    //TODO: When I have interwebs again and can grab the GUI these need to stop being static and lose all these parameters

    // Render string of text
    public static void renderString(Vector2f coord, String s) {
        int row = 0; int column = 0; // Line and character count
    }

    // Render a single character to the screen
    public static void renderCharacter(Vector2d coord, byte character) {
        Vector2f[] texture = getCharacterTextureCoordinates(character, 256);
        glBegin(GL_QUADS);
        glTexCoord2f(texture[0].getX(), texture[0].getY());
        glVertex3f(coord.getX(), coord.getY(), 0);
        glTexCoord2f(texture[1].getX(), texture[1].getY());
        glVertex3f(coord.getX() + fontSize*16, coord.getY(), 0);
        glTexCoord2f(texture[2].getX(), texture[2].getY());
        glVertex3f(coord.getX() + fontSize*16, coord.getY() - fontSize*16, 0);
        glTexCoord2f(texture[3].getX(), texture[3].getY());
        glVertex3f(coord.getX(), coord.getY() - fontSize*16, 0);
        glEnd();
    }

    // Get the screen coordinates for rendering a character
    Vector2f[] getCharacterVertexCoordinates(Vector2f coord) {
        // Array of coordinates to return
        Vector2f[] coordinates = new Vector2f[4];
        coordinates[0] = coord;
        coordinates[1] = new Vector2f();
        return coordinates;
    }

    //TODO: It may be smarter to just index all 256 possible results, rather than generating them over and over..
    // Get the texture coordinates of the 4 corners of any ascii character
    static Vector2f[] getCharacterTextureCoordinates(byte ascii, int resolution) {
        // Array of coordinates to return
        Vector2f[] coordinates = new Vector2f[4];
        // Characters per row, since the image is square this is also characters per column but whatever.
        int characters_per_row = 16;
        int character_resolution = resolution / characters_per_row;
        double row = Math.floor(ascii/characters_per_row);
        int column = ascii%characters_per_row;

        // Get the floating point percentage of sorts for each individual characters. This should come out to 0.0625
        // Basically if an image's width is 1, how much of 1 does each character take up.
        float percent = 1f / characters_per_row;

        // Get the top left coordinates, for easy reference
        float topLeftX = (float)column*percent;
        float topLeftY = (float)row*percent;

        // Assign each corner.
        coordinates[0] = new Vector2f(topLeftX, topLeftY);
        coordinates[1] = new Vector2f(topLeftX + percent, topLeftY);
        coordinates[2] = new Vector2f(topLeftX + percent, topLeftY + percent);
        coordinates[3] = new Vector2f(topLeftX, topLeftY + percent);
        return coordinates;
    }
}