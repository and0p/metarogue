package net.and0.metarogue.model.GUI;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.util.Enumeration;

import net.and0.metarogue.util.settings.DisplaySettings;
import net.and0.metarogue.util.threed.Box;
import net.and0.metarogue.util.threed.Vector2d;

public class GUI {

    public DefaultMutableTreeNode root;
    public DefaultTreeModel gui;

	public GUI() {
        GUIElement rootElement = new GUIElement(0,0, DisplaySettings.resolutionX, DisplaySettings.resolutionY, 0);
		root = new DefaultMutableTreeNode(rootElement);
        gui = new DefaultTreeModel(root);
	}

    public void addElement(GUIElement e) {
        root.add(new DefaultMutableTreeNode(e));
        System.out.print(root.getChildCount() + "\n");
    }
	
	public Enumeration getElementsPreorder() {
        //return root.depthFirstEnumeration();
        return root.preorderEnumeration();
	}

    public void update() {

        Enumeration e = getElementsPreorder();      // Create the enumeration to go through
        Box[] added = new Box[root.getDepth()+1];   // Create an array of Boxes. These will store the added values
        // Initialise boxes
        for(int i = 0; i < added.length; i++) {
            added[i] = new Box();
        }
        int a = 0;

        // Iterate over each element, setting it's position and keeping track of width for parent elements
        while(e.hasMoreElements()) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
            // a++;
            // System.out.print(a +"Updated.\n");
            GUIElement element;
            if(node.getUserObject() != null && node.getLevel() > 0) {
                element = (GUIElement)node.getUserObject();
                int depth = node.getLevel();
                if(element.absolutePosition) {
                    element.setPosition(element.position.getX(), element.position.getY());
                    added[depth].setCorner(0, element.position);
                }
                if(!element.absolutePosition) {
                    // Modify based on alignment
                    if(element.halign == GUIElement.hAlign.LEFT) {
                        element.setX(added[depth - 1].getLeft() + element.margin);
                    }
                    else if(element.halign == GUIElement.hAlign.RIGHT) {
                        element.setX(added[depth - 1].getRight() - element.width);
                    }
                    //element.setPosition(added[depth - 1].getX() + element.margin, added[depth - 1].getY() + element.margin);
                    //added[depth].setCorners(added[depth - 1].getX() + element.margin + element.bordersize + element.padding);
                    // System.out.print("GUIElement updated with position " + (added[depth - 1].getX() + element.margin) +  "x and " + (added[depth - 1].getY() + element.margin) + " y at depth level " + node.getLevel() + ".\n");
                    //System.out.print("Set depth[" + depth + "] to coordinates " + added[depth].getX() + ", " + added[depth].getY() + ".\n");
                }
            }
        }
    }

    public void bullshitAddTest() {
        for (Enumeration e = getElementsPreorder(); e.hasMoreElements();) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
            if(node.getUserObject() != null && node.getLevel() > 0) {
                node.add(new DefaultMutableTreeNode(new GUIElement(0,0,100,100,10)));
            }
        }
        update();
    }

    public GUIElement bullshitGetKid() {
        DefaultMutableTreeNode hello = (DefaultMutableTreeNode)root.getChildAt(0);
        return (GUIElement)hello.getUserObject();
    }

}