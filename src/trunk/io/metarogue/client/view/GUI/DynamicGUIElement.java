package io.metarogue.client.view.GUI;

import io.metarogue.game.gameobjects.GameObject;
import io.metarogue.util.math.Vector2d;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * MetaRogue class
 * User: andrew
 * Date: 1/5/14
 * time: 9:40 PM
 */
public class DynamicGUIElement extends GUIElement {

    // Root of element we'll be displaying for each object
    DefaultMutableTreeNode instance;
    // List of objects to display
    ArrayList<GameObject> objectList;
    // Elements within
    ArrayList<GUIElement> variableElements;

    public DynamicGUIElement() {
        variableElements = new ArrayList<GUIElement>();
    }

    public DynamicGUIElement(DefaultMutableTreeNode instance) {
        setInstance(instance);
        objectList = new ArrayList<GameObject>();
        variableElements = new ArrayList<GUIElement>();
    }

    public DynamicGUIElement(DefaultMutableTreeNode instance, ArrayList<GameObject> objectList) {
        setInstance(instance);
        this.objectList = objectList;
        variableElements = new ArrayList<GUIElement>();
    }

    public void render() {
        // Get element, for simplicity
        GUIElement e = (GUIElement)instance.getUserObject();
        // get total number of elements, get width / height of each element with margins, and figure out row/columns that can fit from that
        int eWidth = e.getWidth() + e.margin[1] + e.margin[3];
        int eHeight = e.getHeight() + e.margin[0] + e.margin[2];
        int columns = (int)Math.floor(((width - padding[1] - padding[3]) / eWidth));
        int rows = (int)Math.floor(((height - padding[0] - padding[2]) / eHeight));
        int displayableElements = columns*rows;
        if(displayableElements > objectList.size()) displayableElements = objectList.size();
        // TODO: Calculations for scrolling
        // Render the window itself, like a normal element
        super.render();
        // If there are no elements or no room to display them, don't bother to do anything else
        if(columns == 0 || rows == 0 || displayableElements == 0) return;
        int currentColumn = 0;
        int currentRow = 0;
        // for (each in array list)
        for(int i = 0; i < displayableElements; i++) {
            GameObject go = objectList.get(i);
            // Set position of element to draw
            Vector2d tempPos = new Vector2d(position.getX()+padding[3]+e.margin[3]+eWidth*currentColumn, position.getY()+padding[0]+e.margin[0]+eHeight*currentRow);
            e.setX(position.getX()+padding[3]+e.margin[3]+eWidth*currentColumn);
            e.setY(position.getY()+padding[0]+e.margin[0]+eHeight*currentRow);
            e.tempBox.setCorners(tempPos, e.width, e.height);
            GUIUpdater.updateElement(instance);
            // update variables for all objects that want them
            for(GUIElement ge : variableElements) {
                if(ge.variable != null) ge.setVariable(go.getVariableObject(ge.variable));
                if(ge.displayObject) ge.setGameObject(go);
            }
            // render that beast
            for (Enumeration en = instance.preorderEnumeration(); en.hasMoreElements();) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) en.nextElement();
                GUIElement g = (GUIElement)node.getUserObject();
                if(g != null) {
                    g.render();
                }
            }
            // Adjust what column and row we're in, if column limit reached reset and go down a row
            currentColumn++;
            if(currentColumn >= columns) {
                currentColumn = 0;
                currentRow++;
            }
        }
    }

    // Method to change element, and therefore check that the enumeration of elements with varaibles is up-to-date
    public void setInstance(DefaultMutableTreeNode d) {
        // Set this instance as the one being passed in
        instance = d;
        // Clear whatever variables elements are currently stored
        variableElements.clear();
        // Add all GUIElements that are asking for variables
        for (Enumeration en = d.preorderEnumeration(); en.hasMoreElements();) {
            DefaultMutableTreeNode n = (DefaultMutableTreeNode)  en.nextElement();
            GUIElement e = (GUIElement) n.getUserObject();
            if(e.variable != null || e.displayObject) variableElements.add(e);
        }
    }

    public DynamicGUIElement copy() {
        DynamicGUIElement copy = new DynamicGUIElement();
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
        // Dynamic mess oh my god
        copy.instance = instance;
        copy.variableElements = variableElements;
        copy.objectList = objectList;
        return copy;
    }

}