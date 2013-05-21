package net.and0.metarogue.view;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex3f;
import net.and0.metarogue.model.GUI.Element;
import net.and0.metarogue.model.GUI.GUI;
import net.and0.metarogue.util.threed.Vector2d;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.Enumeration;

public class GUIRenderer {
	
//	public int padding;
//	public int margin;
//	
//	public int width;
//	public int height;
//	
//	public int xPosition;
//	public int yPosition;
//	
//	public int bordersize;
//	
//	public String text;
//	
//	public boolean active;
//	public boolean visible;
//	public boolean bordered;
	

	public GUIRenderer() {
		// TODO Auto-generated constructor stub
	}
	
	public static void renderGUI(GUI g) {
        for (Enumeration e = g.getElementsDepthFirst(); e.hasMoreElements();) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
            if((Element)node.getUserObject() != null && node.getLevel() > 0) {
                renderElement((Element)node.getUserObject());
            }
        }
	}
	
	public static void renderElement(Element e) {
		
		// Establish a bounding box
		Vector2d[] corners = {  new Vector2d(e.position),
								new Vector2d(e.position.getX() + e.width, e.position.getY()),
								new Vector2d(e.position.getX() + e.width, e.position.getY() - e.height),
								new Vector2d(e.position.getX(), e.position.getY() - e.height)
								};
		
		// Render inside
		if(e.bordersize > 0) {
			//Render center
	        glBegin(GL_QUADS);
	       		glTexCoord2f(.75f,0);
		    	glVertex3f(corners[0].getX() + e.bordersize, corners[0].getY() - e.bordersize, 0);
		    	glTexCoord2f(.75f,0);
		    	glVertex3f(corners[1].getX() - e.bordersize, corners[1].getY() - e.bordersize, 0);
		    	glTexCoord2f(.75f,1);
		    	glVertex3f(corners[2].getX() - e.bordersize, corners[2].getY() + e.bordersize, 0);
		    	glTexCoord2f(.75f,1);
		    	glVertex3f(corners[3].getX() + e.bordersize, corners[3].getY() + e.bordersize, 0);
			glEnd();
			// Top-left corner
	        glBegin(GL_QUADS);
	        	glTexCoord2f(0,0);
		    	glVertex3f(corners[0].getX(), corners[0].getY(), 0);
		    	glTexCoord2f(.25f,0);
		    	glVertex3f(corners[0].getX() + e.bordersize, corners[0].getY(), 0);
		    	glTexCoord2f(.25f,1);
		    	glVertex3f(corners[0].getX() + e.bordersize, corners[0].getY() - e.bordersize, 0);
		    	glTexCoord2f(0,1);
		    	glVertex3f(corners[0].getX(), corners[0].getY() -+ e.bordersize, 0);
			glEnd();
			// Top-right corner
	        glBegin(GL_QUADS);
	        	glTexCoord2f(.25f,0);
		    	glVertex3f(corners[1].getX() - e.bordersize, corners[1].getY(), 0);
		    	glTexCoord2f(0,0);
		    	glVertex3f(corners[1].getX(), corners[1].getY(), 0);
		    	glTexCoord2f(0,1);
		    	glVertex3f(corners[1].getX(), corners[1].getY() - e.bordersize, 0);
		    	glTexCoord2f(.25f,1);
		    	glVertex3f(corners[1].getX() - e.bordersize, corners[1].getY() - e.bordersize, 0);
			glEnd();
			// Bottom-right corner
	        glBegin(GL_QUADS);
	        	glTexCoord2f(.25f,1);
		    	glVertex3f(corners[2].getX() - e.bordersize, corners[2].getY() + e.bordersize, 0);
		    	glTexCoord2f(0,1);
		    	glVertex3f(corners[2].getX(), corners[2].getY() + e.bordersize, 0);
		    	glTexCoord2f(0,0);
		    	glVertex3f(corners[2].getX(), corners[2].getY(), 0);
		    	glTexCoord2f(.25f,0);
		    	glVertex3f(corners[2].getX() - e.bordersize, corners[2].getY(), 0);
			glEnd();
			// Bottom-left corner
	        glBegin(GL_QUADS);
	        	glTexCoord2f(0,1);
		    	glVertex3f(corners[3].getX(), corners[3].getY() + e.bordersize, 0);
		    	glTexCoord2f(.25f,1);
		    	glVertex3f(corners[3].getX() + e.bordersize, corners[3].getY() + e.bordersize, 0);
		    	glTexCoord2f(.25f,0);
		    	glVertex3f(corners[3].getX() + e.bordersize, corners[3].getY(), 0);
		    	glTexCoord2f(0,0);
		    	glVertex3f(corners[3].getX(), corners[3].getY(), 0);
			glEnd();
			// Top side
	        glBegin(GL_QUADS);
	        	glTexCoord2f(.25f,0);
		    	glVertex3f(corners[0].getX() + e.bordersize, corners[0].getY(), 0);
		    	glTexCoord2f(.5f,0);
		    	glVertex3f(corners[1].getX() - e.bordersize, corners[1].getY(), 0);
		    	glTexCoord2f(.5f,1);
		    	glVertex3f(corners[1].getX() - e.bordersize, corners[1].getY() - e.bordersize, 0);
		    	glTexCoord2f(.25f,1);
		    	glVertex3f(corners[0].getX() + e.bordersize, corners[0].getY() - e.bordersize, 0);
			glEnd();
			// Right side
	        glBegin(GL_QUADS);
	        	glTexCoord2f(.25f,1);
	        	glVertex3f(corners[1].getX() - e.bordersize, corners[1].getY() - e.bordersize, 0);
	        	glTexCoord2f(.5f,0);
		    	glVertex3f(corners[1].getX(), corners[1].getY() - e.bordersize, 0);
		    	glTexCoord2f(.5f,0);
		    	glVertex3f(corners[2].getX(), corners[2].getY() + e.bordersize, 0);
		    	glTexCoord2f(.25f,1);
		    	glVertex3f(corners[2].getX() - e.bordersize, corners[2].getY() + e.bordersize, 0);
			glEnd();
			// Bottom side
	        glBegin(GL_QUADS);
	        	glTexCoord2f(.25f,1);
	        	glVertex3f(corners[3].getX() + e.bordersize, corners[3].getY() + e.bordersize, 0);
		    	glTexCoord2f(.5f,1);
		    	glVertex3f(corners[2].getX() - e.bordersize, corners[2].getY() + e.bordersize, 0);
		    	glTexCoord2f(.5f,0);
		    	glVertex3f(corners[2].getX() - e.bordersize, corners[2].getY(), 0);
		    	glTexCoord2f(.25f,0);
		    	glVertex3f(corners[3].getX() + e.bordersize, corners[2].getY(), 0);
			glEnd();
			// Left side
	        glBegin(GL_QUADS);
	        	glTexCoord2f(.5f, 0);
	        	glVertex3f(corners[0].getX(), corners[1].getY() - e.bordersize, 0);
		    	glTexCoord2f(.5f, 1);
		    	glVertex3f(corners[0].getX() + e.bordersize, corners[1].getY() - e.bordersize, 0);
		    	glTexCoord2f(.25f, 1);
		    	glVertex3f(corners[3].getX() + e.bordersize, corners[3].getY() + e.bordersize, 0);
		    	glTexCoord2f(.25f, 0);
		    	glVertex3f(corners[3].getX(), corners[3].getY() + e.bordersize, 0);
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
		
		
	}

}
