package net.and0.metarogue.model.GUI;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.util.Enumeration;

import net.and0.metarogue.util.settings.DisplaySettings;
import net.and0.metarogue.util.threed.Vector2d;

public class GUI {

    public DefaultMutableTreeNode root;
    public DefaultTreeModel gui;

	public GUI() {
        Element rootElement = new Element(0,0, DisplaySettings.resolutionX, DisplaySettings.resolutionY, 0);
		root = new DefaultMutableTreeNode(rootElement);
        gui = new DefaultTreeModel(root);
	}

    public void addElement(Element e) {
        root.add(new DefaultMutableTreeNode(e));
        System.out.print(root.getChildCount() + "\n");
    }
	
	public Enumeration getElementsPreorder() {
        //return root.depthFirstEnumeration();
        return root.preorderEnumeration();
	}

    public void update() {
        // Create the enumeration to go through
        Enumeration e = getElementsPreorder();
        // Create an array of Vector2d. These will store the added
        Vector2d[] added = new Vector2d[root.getDepth()+1];
        for(int i = 0; i < added.length; i++) {
            added[i] = new Vector2d(0,0);
        }
        int a = 0;
        while(e.hasMoreElements()) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
            a++;
            //System.out.print(a +"Updated.\n");
            Element element;
            if(node.getUserObject() != null && node.getLevel() > 0) {
                element = (Element)node.getUserObject();
                int depth = node.getLevel();
                if(element.absolutePosition) {
                    element.setPosition(element.position.getX(), element.position.getY());
                    added[depth].setX(element.position.getX() + element.bordersize + element.padding);
                    added[depth].setY(element.position.getY() + element.bordersize + element.padding);
                }
                if(!element.absolutePosition) {
                    element.setPosition(added[depth - 1].getX() + element.margin, added[depth - 1].getY() + element.margin);
                    added[depth].setX(added[depth - 1].getX() + element.position.getX() + element.margin + element.bordersize + element.padding);
                    added[depth].setY(added[depth - 1].getY() + element.position.getY() + element.margin + element.bordersize + element.padding);
                    System.out.print("Element updated with position " + (added[depth - 1].getX() + element.margin) +  "x and " + (added[depth - 1].getY() + element.margin) + " y at depth level " + node.getLevel() + ".\n");
                    System.out.print("Set depth[" + depth + "] to coordinates " + added[depth].getX() + ", " + added[depth].getY() + ".\n");
                }
            }
        }
    }

    public void bullshitAddTest() {
        for (Enumeration e = getElementsPreorder(); e.hasMoreElements();) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
            if(node.getUserObject() != null && node.getLevel() > 0) {
                node.add(new DefaultMutableTreeNode(new Element(0,0,100,100,10)));
            }
        }
        update();
    }

    public Element bullshitGetKid() {
        DefaultMutableTreeNode hello = (DefaultMutableTreeNode)root.getChildAt(0);
        return (Element)hello.getUserObject();
    }

}