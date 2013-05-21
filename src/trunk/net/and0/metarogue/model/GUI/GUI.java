package net.and0.metarogue.model.GUI;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.util.Enumeration;
import net.and0.metarogue.model.GUI.Element;
import net.and0.metarogue.util.settings.DisplaySettings;
import net.and0.metarogue.util.threed.Vector2d;
import org.lwjgl.opengl.Display;

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
	
	public Enumeration getElementsDepthFirst() {
        return root.depthFirstEnumeration();
        //return root.preorderEnumeration();
	}

    public void update() {
        // Create the enumeration to go through
        Enumeration e = getElementsDepthFirst();
        // Create an array of Vector2d. These will store the added
        Vector2d[] added = new Vector2d[16];
        for(int i = 0; i < added.length; i++) {
            added[i] = new Vector2d(40,40);
        }
        if(e.hasMoreElements()) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
            Element element = (Element)node.getUserObject();
            if(node.getUserObject() != null && node.getLevel() > 0) {
                int depth = node.getLevel();
                added[depth].setX(added[depth - 1].getX() + element.margin + element.bordersize + element.padding);
                added[depth].setY(added[depth - 1].getY() + element.margin + element.bordersize + element.padding);
                element.position.setX(added[depth-1].getX() + element.margin);
                element.position.setY(added[depth - 1].getY() + element.margin);
            }
        }
    }

    public void bullshitAddTest() {
        for (Enumeration e = getElementsDepthFirst(); e.hasMoreElements();) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
            if((Element)node.getUserObject() != null && node.getLevel() > 0) {
                node.add(new DefaultMutableTreeNode(new Element(0,0,30,30,10)));
            }
        }
    }

}
