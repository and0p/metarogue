package io.metarogue.client.view.GUI;

import io.metarogue.Main;
import io.metarogue.game.gameobjects.GameObject;
import io.metarogue.game.gameobjects.GameVariable;
import io.metarogue.client.view.threed.Box;
import io.metarogue.client.view.threed.Vector2d;
import io.metarogue.client.view.OpenGLRenderer;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex3f;

public abstract class GUIElement {

    public String id;                           // ID of this element

    public GameObject obj;                      // Game object this element is getting values from
    public String variable;                     // String of variable it should be trying to pull from an object
    public GameVariable gameVariable;           // GameVariable object element should represent

    public boolean displayObject = false;       // Should this element display the GameObject it's assigned? Useful for inventory, profile, etc.

    public int[] margin = new int[]{0,0,0,0};   // Margin in pixels, top/right/bottom/left
    public int[] padding = new int[]{5,5,5,5};  // Internal padding in pixels, top/right/bottom/left

    public int width = 30;                      // Width in pixels
    public int height = 30;                     // Height in pixels

    public int[] borderSize = {5,5,5,5};        // Size of border in pixels, top/right/bottom/left

    // Corner type
    public static enum cornerType { BOX, CHAMFER, ROUND, PIXEL; }
    public cornerType cornertype = cornerType.PIXEL;

    public static enum orientationType { HORIZONTAL, VERTICAL; }
    public orientationType orientation = orientationType.HORIZONTAL;

    public float[] borderColor = {0.2f,0.2f,0.2f};      // Color of border
    public float[] textColor = {1f,1f,1f};              // Color of text
    public float[] fillColor = {1f,0f,0f};              // Color of value "fill"
    public float[] backColor = {.05f,.05f,.05f};        // Color of background

    public boolean wrap = true;                 // Text wrapping

    public boolean absolutePosition = false;    // Position type, absolute vs relative

    public Vector2d position;                   // Ideal absolute / relative position, x and y in pixels
    public Box tempBox = new Box();             // Actual bounding box of element after absolute / relative placement

    public String text;                         // Text to display in this element
    public int fontSize = 1;                    // Amount to multiply the original (presumably) 16x16 font

    public boolean active = true;               // Whether or not element is active
    public boolean visible = true;              // Whether or not element is visible

    public int depth = 0;                       // Depth of this element in tree
    public int place = 0;                       // Place of this element amongst children. (0 would be left-most, I'd assume.)

    // Alignment horizontally
    public static enum hAlign { LEFT, RIGHT, CENTER; }
    // Alignment vertically
    public static enum vAlign { TOP, BOTTOM, CENTER; }
    // Position type
    public static enum positionType { INLINE, RELATIVE, ABSOLUTE; }

    public hAlign halign = hAlign.LEFT;
    public vAlign valign = vAlign.TOP;
    public positionType positiontype = positionType.RELATIVE;

    public GUIElement() {
        position = new Vector2d(0, 0);
    }

    public GUIElement(int x, int y, int width, int height, int borderSize) {
        position = new Vector2d(x, y);
        this.width = width;
        this.height = height;
        this.borderSize[0] = borderSize;
        this.borderSize[1] = borderSize;
        this.borderSize[2] = borderSize;
        this.borderSize[3] = borderSize;
    }

    public GUIElement(int x, int y, int width, int height, int borderSize, boolean absolutePosition) {
        position = new Vector2d(x, y);
        this.width = width;
        this.height = height;
        this.borderSize[0] = borderSize;
        this.borderSize[1] = borderSize;
        this.borderSize[2] = borderSize;
        this.borderSize[3] = borderSize;
        this.absolutePosition = absolutePosition;
    }

    public String getId() {
        return id;
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

        if(visible) {
            int displayHeight = Display.getHeight(); //TODO make this smarter. Or something, no need to grab this every time I render A Thing.

            // Establish a bounding box
            Vector2d[] corners = {  tempBox.getCorner(0), tempBox.getCorner(1), tempBox.getCorner(2), tempBox.getCorner(3)  };
            // Subtract display height from Y so it sticks to the to top instead of the bottom
            for(int i = 0; i < 4; i++) {
                corners[i].setY(displayHeight - corners[i].getY());
            }

            glBegin(GL_QUADS);      // Begin rendering
            glTexCoord2f(0f,0f);    // Set texture to 0,0 for pure white

            // Render the borders, first setting color
            glColor4f(borderColor[0], borderColor[1], borderColor[2], 1.0f);

            // Top border
            if(borderSize[0] > 0){
                glVertex3f(corners[0].getX() + borderSize[3], corners[0].getY(), 0);
                glVertex3f(corners[1].getX() - borderSize[1], corners[1].getY(), 0);
                glVertex3f(corners[1].getX() - borderSize[1], corners[1].getY() - borderSize[0], 0);
                glVertex3f(corners[0].getX() + borderSize[3], corners[0].getY() - borderSize[0], 0);
            }
            // Right border
            if(borderSize[1] > 0){
                glVertex3f(corners[1].getX() - borderSize[1], corners[1].getY() - borderSize[0], 0);
                glVertex3f(corners[1].getX(), corners[1].getY() - borderSize[0], 0);
                glVertex3f(corners[2].getX(), corners[2].getY() + borderSize[2], 0);
                glVertex3f(corners[2].getX() - borderSize[1], corners[2].getY() + borderSize[2], 0);
            }
            // Bottom border
            if(borderSize[2] > 0){
                glVertex3f(corners[3].getX() + borderSize[3], corners[2].getY() + borderSize[2], 0);
                glVertex3f(corners[2].getX() - borderSize[1], corners[2].getY() + borderSize[2], 0);
                glVertex3f(corners[2].getX() - borderSize[1], corners[3].getY(), 0);
                glVertex3f(corners[3].getX() + borderSize[3], corners[3].getY(), 0);
            }
            // Left border
            if(borderSize[3] > 0){
                glVertex3f(corners[0].getX(), corners[0].getY() - borderSize[0], 0);
                glVertex3f(corners[0].getX() + borderSize[3], corners[0].getY() - borderSize[0], 0);
                glVertex3f(corners[3].getX() + borderSize[3], corners[3].getY() + borderSize[2], 0);
                glVertex3f(corners[3].getX(), corners[3].getY() + borderSize[2], 0);
            }

            glEnd();

            // Render the corners

            if(cornertype == cornerType.BOX) {
                glBegin(GL_QUADS);
                // Top-left
                if(borderSize[3] > 0 && borderSize[0] > 0) {
                    glVertex3f(corners[0].getX(), corners[0].getY(), 0);
                    glVertex3f(corners[0].getX() + borderSize[3], corners[0].getY(), 0);
                    glVertex3f(corners[0].getX() + borderSize[3], corners[0].getY() - borderSize[0], 0);
                    glVertex3f(corners[0].getX(), corners[0].getY() - borderSize[0], 0);
                }
                // Top-right
                if(borderSize[0] > 0 && borderSize[1] > 0) {
                    glVertex3f(corners[1].getX()  - borderSize[1], corners[1].getY(), 0);
                    glVertex3f(corners[1].getX(), corners[1].getY(), 0);
                    glVertex3f(corners[1].getX(), corners[1].getY() - borderSize[0], 0);
                    glVertex3f(corners[1].getX()  - borderSize[1], corners[1].getY() - borderSize[0], 0);
                }
                // Bottom-right
                if(borderSize[1] > 0 && borderSize[2] > 0) {
                    glVertex3f(corners[2].getX()  - borderSize[1], corners[2].getY() + borderSize[2], 0);
                    glVertex3f(corners[2].getX(), corners[2].getY() + borderSize[2], 0);
                    glVertex3f(corners[2].getX(), corners[2].getY(), 0);
                    glVertex3f(corners[2].getX()  - borderSize[1], corners[2].getY(), 0);
                }
                // Bottom-left
                if(borderSize[3] > 0 && borderSize[2] > 0) {
                    glVertex3f(corners[3].getX(), corners[3].getY() + borderSize[2], 0);
                    glVertex3f(corners[3].getX() + borderSize[3], corners[3].getY() + borderSize[2], 0);
                    glVertex3f(corners[3].getX() + borderSize[3], corners[3].getY(), 0);
                    glVertex3f(corners[3].getX(), corners[3].getY(), 0);
                }
                glEnd();
            }

            if(cornertype == cornerType.CHAMFER) {
                glBegin(GL_TRIANGLES);
                // Top-left
                if(borderSize[3] > 0 && borderSize[0] > 0) {
                    glVertex3f(corners[0].getX() + borderSize[3], corners[0].getY(), 0);
                    glVertex3f(corners[0].getX() + borderSize[3], corners[0].getY() - borderSize[0], 0);
                    glVertex3f(corners[0].getX(), corners[0].getY() - borderSize[0], 0);
                }
                // Top-right
                if(borderSize[0] > 0 && borderSize[1] > 0) {
                    glVertex3f(corners[1].getX()  - borderSize[1], corners[1].getY(), 0);
                    glVertex3f(corners[1].getX(), corners[1].getY() - borderSize[0], 0);
                    glVertex3f(corners[1].getX()  - borderSize[1], corners[1].getY() - borderSize[0], 0);
                }
                // Bottom-right
                if(borderSize[1] > 0 && borderSize[2] > 0) {
                    glVertex3f(corners[2].getX()  - borderSize[1], corners[2].getY() + borderSize[2], 0);
                    glVertex3f(corners[2].getX(), corners[2].getY() + borderSize[2], 0);
                    glVertex3f(corners[2].getX()  - borderSize[1], corners[2].getY(), 0);
                }
                // Bottom-left
                if(borderSize[3] > 0 && borderSize[2] > 0) {
                    glVertex3f(corners[3].getX(), corners[3].getY() + borderSize[2], 0);
                    glVertex3f(corners[3].getX() + borderSize[3], corners[3].getY() + borderSize[2], 0);
                    glVertex3f(corners[3].getX() + borderSize[3], corners[3].getY(), 0);
                }
                glEnd();
            }

            if(cornertype == cornerType.PIXEL) {
                glBegin(GL_QUADS);
                // Top-left
                if(borderSize[3] > 0 && borderSize[0] > 0) {
                    glVertex3f(corners[0].getX() + borderSize[3]/2, corners[0].getY() - borderSize[0]/2, 0);
                    glVertex3f(corners[0].getX() + borderSize[3],   corners[0].getY() - borderSize[0]/2, 0);
                    glVertex3f(corners[0].getX() + borderSize[3],   corners[0].getY() - borderSize[0], 0);
                    glVertex3f(corners[0].getX() + borderSize[3]/2, corners[0].getY() - borderSize[0], 0);
                }
                // Top-right
                if(borderSize[0] > 0 && borderSize[1] > 0) {
                    glVertex3f(corners[1].getX() - borderSize[1],   corners[1].getY() - borderSize[0]/2, 0);
                    glVertex3f(corners[1].getX() - borderSize[1]/2, corners[1].getY() - borderSize[0]/2, 0);
                    glVertex3f(corners[1].getX() - borderSize[1]/2, corners[1].getY() - borderSize[0], 0);
                    glVertex3f(corners[1].getX()  - borderSize[1],  corners[1].getY() - borderSize[0], 0);
                }
                // Bottom-right
                if(borderSize[1] > 0 && borderSize[2] > 0) {
                    glVertex3f(corners[2].getX()  - borderSize[1],  corners[2].getY() + borderSize[2], 0);
                    glVertex3f(corners[2].getX() - borderSize[1]/2, corners[2].getY() + borderSize[2], 0);
                    glVertex3f(corners[2].getX() - borderSize[1]/2, corners[2].getY() + borderSize[2]/2, 0);
                    glVertex3f(corners[2].getX()  - borderSize[1],  corners[2].getY() + borderSize[2]/2, 0);
                }
                // Bottom-left
                if(borderSize[3] > 0 && borderSize[2] > 0) {
                    glVertex3f(corners[3].getX() + borderSize[3]/2, corners[3].getY() + borderSize[2], 0);
                    glVertex3f(corners[3].getX() + borderSize[3],   corners[3].getY() + borderSize[2], 0);
                    glVertex3f(corners[3].getX() + borderSize[3],   corners[3].getY() + borderSize[2]/2, 0);
                    glVertex3f(corners[3].getX() + borderSize[3]/2, corners[3].getY() + borderSize[2]/2, 0);
                }
                glEnd();
            }

            // Render the inside
            // Show as bar if a variable is attached
            if(gameVariable != null) {
                if(orientation == orientationType.HORIZONTAL) {
                    // Get percentage of variable filling, make sure it's within limits.
                    float percent = gameVariable.getPercentage();
                    if(percent > 1) percent = 1;
                    if(percent < 0) percent = 0;
                    // Get that in pixels from the left side
                    int edge = (int)((width - borderSize[1] - borderSize[3]) * percent);
                    glBegin(GL_QUADS);
                    // Draw fill
                    glColor4f(fillColor[0], fillColor[1], fillColor[2], 1.0f);
                    glVertex3f(corners[0].getX() + borderSize[3], corners[0].getY() - borderSize[0], 0);
                    glVertex3f(corners[0].getX() + borderSize[3] + edge, corners[1].getY() - borderSize[0], 0);
                    glVertex3f(corners[3].getX() + borderSize[3] + edge, corners[2].getY() + borderSize[2], 0);
                    glVertex3f(corners[3].getX() + borderSize[3], corners[3].getY() + borderSize[2], 0);
                    // Draw background
                    glColor4f(backColor[0], backColor[1], backColor[2], 1.0f);
                    glVertex3f(corners[0].getX() + borderSize[3] + edge, corners[1].getY() - borderSize[0], 0);
                    glVertex3f(corners[1].getX() - borderSize[1], corners[1].getY() - borderSize[0], 0);
                    glVertex3f(corners[2].getX() - borderSize[1], corners[2].getY() + borderSize[2], 0);
                    glVertex3f(corners[3].getX() + borderSize[3] + edge, corners[3].getY() + borderSize[2], 0);
                    glEnd();
                }
            } else {
                glBegin(GL_QUADS);
                glColor4f(backColor[0], backColor[1], backColor[2], 1.0f);
                glVertex3f(corners[0].getX() + borderSize[3], corners[0].getY() - borderSize[0], 0);
                glVertex3f(corners[1].getX() - borderSize[1], corners[1].getY() - borderSize[0], 0);
                glVertex3f(corners[2].getX() - borderSize[1], corners[2].getY() + borderSize[2], 0);
                glVertex3f(corners[3].getX() + borderSize[3], corners[3].getY() + borderSize[2], 0);
                glEnd();
            }

            // Render the text
            renderString();

            // Render assigned object, if property is set
            if(displayObject && obj != null) {
                int i = 10;
                // Check if object has a texture to render at all
                if(obj.texture != null) {
                    OpenGLRenderer.bindTextureLoRes(obj.texture);
                    glBegin(GL_QUADS);
                    glColor4f(GL_ONE, GL_ONE, GL_ONE, GL_ONE);
                    glTexCoord2f(0,0);
                    glVertex3f(corners[0].getX() + borderSize[3] + padding[3], corners[0].getY() - borderSize[0] - padding[0], 0);
                    glTexCoord2f(1,0);
                    glVertex3f(corners[1].getX() - borderSize[1] - padding[1], corners[1].getY() - borderSize[0] - padding[1], 0);
                    glTexCoord2f(1,1);
                    glVertex3f(corners[2].getX() - borderSize[1] - padding[1], corners[2].getY() + borderSize[2] + padding[2], 0);
                    glTexCoord2f(0,1);
                    glVertex3f(corners[3].getX() + borderSize[3] + padding[3], corners[3].getY() + borderSize[2] + padding[2], 0);
                    glEnd();
                    OpenGLRenderer.bindTextureLoRes(Main.getGame().getGuiTexture());
                }
            }

            // Reset colors
            glBegin(GL_TRIANGLES);
            glColor4f(GL_ONE, GL_ONE, GL_ONE, GL_ONE);
            glEnd();
        }

    }

    // Render string of text
    void renderString() {
        if(text != null) {
            // Get the initial placement of first character, and get variables
            Vector2d coord = new Vector2d(tempBox.getLeft() + borderSize[3] + padding[3], tempBox.getTop() - borderSize[0] - padding[0]);
            int gWidth = (width - borderSize[1] - padding[1] - borderSize[3] - padding [3]);
            int gHeight = (height - borderSize[0] - padding[0] - borderSize[2] - padding [2]);
            int row = 0; int column = 0; // Line and character count
            byte b;
            // Get the max rows and columns for current element size
            int maxColumns = (int)Math.floor((gWidth / (16*fontSize)));
            int maxRows = (int)Math.floor((gHeight / (16*fontSize)));
            // Begin rendering
            glBegin(GL_QUADS);
            glColor4f(textColor[0], textColor[1], textColor[2], 1.0f);
            for(int i = 0; i < text.length(); i++) {
                // Get string character at index as ASCII byte
                b = (byte)text.charAt(i);
                // Break if the text has gone beyond the rows we can display
                if(row > maxRows-1) break;
                // Check to see if the next character will fit in this row at all, skip only if character not a carriage return anyway (wouldn't want it skipping twice)
                if((column+1)*(fontSize*16) > gWidth && b != 13 && b != 10 && wrap) {
                    // Wrap the line, if enabled
                    row++;
                    column = 0;
                } else if ((column+1)*(fontSize*16) > gWidth && b != 13 && b != 10 && !wrap) {
                    // Keep going until you hit a carriage return, otherwise there's nothing else to display
                    while(i < text.length() || b != 13 || b != 10) {
                        i++;
                        b = (byte)text.charAt(i);
                    }
                } else {
                    // Check what type of character and render or modify as needed
                    if(b == 13 || b == 10) {
                        // If it's a carriage return, move to next row and set column back to beginning
                        row++;
                        column = 0;
                    } else if (b == 32) {
                        // If it's a space, just push along one column
                        column++;
                    } else if (b == '#') {
                        // Escape character, check if character after is
                        int hello = 0;
                    } else if (b > 32) {
                        // Normal character, render at current row and column
                        renderCharacterQuad(coord.getX() + (column * (16 * fontSize)), coord.getY() - (row * (16 * fontSize)), b);
                        column++;
                    }
                }
            }
            glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            glEnd();
        }
    }

    // Render a single character to the screen
    void renderCharacter(int x, int y, byte character) {
        Vector2f[] texture = getCharacterTextureCoordinates(character);
        glBegin(GL_QUADS);
        glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        glTexCoord2f(texture[0].getX(), texture[0].getY());
        glVertex3f(x, y, 0);
        glTexCoord2f(texture[1].getX(), texture[1].getY());
        glVertex3f(x + fontSize*16, y, 0);
        glTexCoord2f(texture[2].getX(), texture[2].getY());
        glVertex3f(x + fontSize*16, y - fontSize*16, 0);
        glTexCoord2f(texture[3].getX(), texture[3].getY());
        glVertex3f(x, y - fontSize*16, 0);
        glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        glEnd();
    }

    // Render a single character to the screen, without the extra OpenGL calls
    void renderCharacterQuad(int x, int y, byte character) {
        Vector2f[] texture = getCharacterTextureCoordinates(character);
        glTexCoord2f(texture[0].getX(), texture[0].getY());
        glVertex3f(x, y, 0);
        glTexCoord2f(texture[1].getX(), texture[1].getY());
        glVertex3f(x + fontSize*16, y, 0);
        glTexCoord2f(texture[2].getX(), texture[2].getY());
        glVertex3f(x + fontSize*16, y - fontSize*16, 0);
        glTexCoord2f(texture[3].getX(), texture[3].getY());
        glVertex3f(x, y - fontSize*16, 0);
    }

    // Render a single character to the screen
    void renderCharacter(Vector2d coord, byte character) {
        renderCharacter(coord.getX(), coord.getY(), character);
    }

    //TODO: It may be smarter to just index all 256 possible results, rather than generating them over and over..
    // Get the texture coordinates of the 4 corners of any ascii character
    static Vector2f[] getCharacterTextureCoordinates(byte ascii) {
        // Array of coordinates to return
        Vector2f[] coordinates = new Vector2f[4];
        // Characters per row, since the image is square this is also characters per column but whatever.
        int characters_per_row_and_column = 16;
        double row = Math.floor(ascii/characters_per_row_and_column);
        int column = ascii%characters_per_row_and_column;

        // Get the floating point percentage of sorts for each individual characters. This should come out to 0.0625
        // Basically if an image's width is 1, how much of 1 does each character take up.
        float percent = 1f / characters_per_row_and_column;

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

    // ENDLESS GETTERS AND SETTERS

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void changeWidth(int c) { this.width += c; };

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void changeHeight(int c) { this.height += c; };

    public void setText(String s) {  text = s; }

    public String getText() { return text; }


    public int[] getBorderSize() {
        return borderSize;
    }

    // A few different ways to set border size
    public void setBorderSize(int[] borderSize) {
        this.borderSize = borderSize;
    }

    public void setBorderSize(int top, int right, int bottom, int left) {
        this.borderSize[0] = top;
        this.borderSize[1] = right;
        this.borderSize[2] = bottom;
        this.borderSize[3] = left;
    }

    public void setBorderSize(int vert, int hor) {
        this.borderSize[0] = vert;
        this.borderSize[1] = hor;
        this.borderSize[2] = vert;
        this.borderSize[3] = hor;
    }

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public GameObject getGameObject() {
        return obj;
    }

    public void setGameObject(GameObject obj) {
        this.obj = obj;
    }

    public GameVariable getVariable() {
        return gameVariable;
    }

    public void setVariable(GameVariable gv) {
        this.gameVariable = gv;
    }

}