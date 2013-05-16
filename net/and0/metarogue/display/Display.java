package net.and0.metarogue.display;

import org.lwjgl.opengl.*;
import org.lwjgl.*;

public class Display {
	
	// Object glDisplay = new org.lwjgl.opengl.Display;

	public Display() {
		try {
			org.lwjgl.opengl.Display.setDisplayMode(new DisplayMode(640, 480));
			org.lwjgl.opengl.Display.setTitle("Yo");
			org.lwjgl.opengl.Display.create();
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		

	}

}
