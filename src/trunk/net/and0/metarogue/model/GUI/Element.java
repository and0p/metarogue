package net.and0.metarogue.model.GUI;

import java.util.List;

import net.and0.metarogue.util.settings.DisplaySettings;
import net.and0.metarogue.util.threed.Vector2d;

public class Element {
	
	public int padding = 5;
	public int margin;
	
	public int width;
	public int height;
	
	public Vector2d position;
	
	public int bordersize;
	
	public String text;
	
	public boolean active;
	public boolean visible;

	public Element(int x, int y, int width, int height, int bordersize) {
		position = new Vector2d(x, DisplaySettings.resolutionY - y);
		this.width = width;
		this.height = height;
		this.bordersize = bordersize;
	}
}
