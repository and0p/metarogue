package io.metarogue.client.view.GUI;

import io.metarogue.client.view.GUI.GUI;
import io.metarogue.client.view.GUI.GUIElement;
import io.metarogue.client.view.threed.Box;
import io.metarogue.client.view.threed.Vector2d;
import org.lwjgl.opengl.Display;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.Enumeration;


public class GUIUpdater {

    public GUIUpdater() {
        // Auto-generated constructor
    }

    public static void updateGUI(GUI gui) {

        Enumeration e = gui.getElementsPreorder();      // Create the enumeration to go through
        Box[] added = new Box[gui.root.getDepth()+1];   // Create an array of Boxes. These will store the added values
        // Initialise boxes. 0 position in array is root, make it screen size.
        added[0] = new Box(new Vector2d(0,0), Display.getWidth(), Display.getHeight());
        for(int i = 1; i < added.length; i++) {
            added[i] = new Box();
        }

        DefaultMutableTreeNode node;
        GUIElement element;
        Vector2d tempPos = new Vector2d(0,0);

        // Iterate over each element, setting it's position and keeping track of width for parent elements
        while(e.hasMoreElements()) {
            // Cast node to an element
            node = (DefaultMutableTreeNode) e.nextElement();
            // If node has a user object and it's higher than the root level, update it
            if(node.getUserObject() != null && node.getLevel() > 0) {
                element = (GUIElement)node.getUserObject();
                int depth = node.getLevel();
                int pDepth = depth-1;
                if(element.absolutePosition) {
                    element.tempBox.setCorners(element.position, element.width, element.height);
                    added[depth] = offsetAbsoluteElement(element, element.tempBox);
                }
                if(!element.absolutePosition) {
                    // Modify based on alignment horizontally
                    if(element.halign == GUIElement.hAlign.LEFT) {
                        tempPos.setX(added[pDepth].getLeft() + element.position.getX() + element.margin[3]);
                    }
                    else if(element.halign == GUIElement.hAlign.CENTER) {
                        int tempX = added[pDepth].getLeft() + ((added[pDepth].getWidth()/2) - (element.width/2));
                        if(tempX < added[pDepth].getLeft()) tempX = added[pDepth].getLeft() + element.margin[3];
                        tempPos.setX(tempX);
                    }
                    else if(element.halign == GUIElement.hAlign.RIGHT) {
                        int tempX = added[pDepth].getRight() - element.position.getX() - element.width - (element.margin[1]);
                        if(tempX < added[pDepth].getLeft()) tempX = added[pDepth].getLeft() + element.margin[3];
                        tempPos.setX(tempX);
                    }
                    // Modify based on alignment vertically
                    if(element.valign == GUIElement.vAlign.TOP) {
                        tempPos.setY(added[pDepth].getTop() + element.position.getY() + element.margin[0]);
                    }
                    else if(element.halign == GUIElement.hAlign.CENTER) {
                        int tempY = added[pDepth].getTop() + ((added[pDepth].getHeight()/2) - (element.height/2));
                        if(tempY < added[pDepth].getTop()) tempY = element.position.getY();
                        tempPos.setY(tempY);
                    }
                    else if(element.valign == GUIElement.vAlign.BOTTOM) {
                        int tempY = added[pDepth].getBottom() - element.position.getY() - element.height - (element.margin[2]);
                        if(tempY < added[pDepth].getTop()) tempY = element.position.getY();
                        tempPos.setY(tempY);
                    }
                    element.tempBox.setCorners(tempPos, element.width, element.height);
                    added[depth] = offsetElement(element, element.tempBox);
                    element.depth = depth;
                    //element.place = node.getIndex();
                }
            }
        }
    }

    public static void updateElement(DefaultMutableTreeNode d) {

        Enumeration e = d.preorderEnumeration();      // Create the enumeration to go through
        GUIElement rootElement = (GUIElement)d.getUserObject();
        Box[] added = new Box[d.getDepth()+1];   // Create an array of Boxes. These will store the added values
        // Initialise boxes. 0 position in array is root, make it screen size.
        added[0] = new Box(rootElement.position, rootElement.width, rootElement.height);
        for(int i = 1; i < added.length; i++) {
            added[i] = new Box();
        }

        DefaultMutableTreeNode node;
        GUIElement element;
        Vector2d tempPos = new Vector2d(0,0);

        // Iterate over each element, setting it's position and keeping track of width for parent elements
        while(e.hasMoreElements()) {
            // Cast node to an element
            node = (DefaultMutableTreeNode) e.nextElement();
            // If node has a user object and it's higher than the root level, update it
            if(node.getUserObject() != null && node.getLevel() > 0) {
                element = (GUIElement)node.getUserObject();
                int depth = node.getLevel();
                int pDepth = depth-1;
                if(element.absolutePosition) {
                    element.tempBox.setCorners(element.position, element.width, element.height);
                    added[depth] = offsetAbsoluteElement(element, element.tempBox);
                }
                if(!element.absolutePosition) {
                    // Modify based on alignment horizontally
                    if(element.halign == GUIElement.hAlign.LEFT) {
                        tempPos.setX(added[pDepth].getLeft() + element.position.getX() + element.margin[3]);
                    }
                    else if(element.halign == GUIElement.hAlign.CENTER) {
                        int tempX = added[pDepth].getLeft() + ((added[pDepth].getWidth()/2) - (element.width/2));
                        if(tempX < added[pDepth].getLeft()) tempX = added[pDepth].getLeft() + element.margin[3];
                        tempPos.setX(tempX);
                    }
                    else if(element.halign == GUIElement.hAlign.RIGHT) {
                        int tempX = added[pDepth].getRight() - element.position.getX() - element.width - (element.margin[1]);
                        if(tempX < added[pDepth].getLeft()) tempX = added[pDepth].getLeft() + element.margin[3];
                        tempPos.setX(tempX);
                    }
                    // Modify based on alignment vertically
                    if(element.valign == GUIElement.vAlign.TOP) {
                        tempPos.setY(added[pDepth].getTop() + element.position.getY() + element.margin[0]);
                    }
                    else if(element.halign == GUIElement.hAlign.CENTER) {
                        int tempY = added[pDepth].getTop() + ((added[pDepth].getHeight()/2) - (element.height/2));
                        if(tempY < added[pDepth].getTop()) tempY = element.position.getY();
                        tempPos.setY(tempY);
                    }
                    else if(element.valign == GUIElement.vAlign.BOTTOM) {
                        int tempY = added[pDepth].getBottom() - element.position.getY() - element.height - (element.margin[2]);
                        if(tempY < added[pDepth].getTop()) tempY = element.position.getY();
                        tempPos.setY(tempY);
                    }
                    element.tempBox.setCorners(tempPos, element.width, element.height);
                    added[depth] = offsetElement(element, element.tempBox);
                    element.depth = depth;
                    //element.place = node.getIndex();
                }
            }
        }
    }

    public static Box offsetElement(GUIElement e, Box parentBox) {
        // Offset the top-left vector of the new box
        Vector2d topLeft = new Vector2d(parentBox.getCorner(0).getX() + e.margin[3] + e.padding[3] + e.borderSize[3],
                parentBox.getCorner(0).getY() + e.margin[0] + e.padding[0] + e.borderSize[0]);
        // Offset the bottom-right vector of the new box
        Vector2d bottomRight = new Vector2d(parentBox.getCorner(2).getX() - e.margin[1] - e.padding[1] - e.borderSize[1],
                parentBox.getCorner(2).getY() - e.margin[2] - e.padding[2] - e.borderSize[2]);
        Box box = new Box(topLeft, bottomRight);
        return box;
    }

    public static Box offsetAbsoluteElement(GUIElement e, Box parentBox) {
        // Offset the top-left vector of the new box
        Vector2d topLeft = new Vector2d(parentBox.getCorner(0).getX()+ e.padding[3] + e.borderSize[3],
                parentBox.getCorner(0).getY() + e.padding[0] + e.borderSize[0]);
        // Offset the bottom-right vector of the new box
        Vector2d bottomRight = new Vector2d(parentBox.getCorner(2).getX() - e.padding[1] - e.borderSize[1],
                parentBox.getCorner(2).getY() - e.padding[2] - e.borderSize[2]);
        Box box = new Box(topLeft, bottomRight);
        return box;
    }



}