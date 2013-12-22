package net.and0.metarogue.view.GUI;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.util.Enumeration;

import net.and0.metarogue.util.settings.DisplaySettings;
import org.lwjgl.opengl.Display;

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

    public void bullshitAddTest() {
        for (Enumeration e = getElementsPreorder(); e.hasMoreElements();) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
            if(node.getUserObject() != null && node.getLevel() > 0) {
                node.add(new DefaultMutableTreeNode(new GUIElement(0,0,100,100,10)));
            }
        }
    }

    public GUIElement bullshitGetKid() {
        DefaultMutableTreeNode hello = (DefaultMutableTreeNode)root.getChildAt(0);
        return (GUIElement)hello.getUserObject();
    }

    public void render() {
        int displayHeight = Display.getHeight();
        for (Enumeration e = getElementsPreorder(); e.hasMoreElements();) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
            GUIElement g = (GUIElement)node.getUserObject();
            if(g != null && node.getLevel() > 0) {
                g.render();
            }
        }
    }

}