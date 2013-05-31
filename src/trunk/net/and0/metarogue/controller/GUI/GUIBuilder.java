package net.and0.metarogue.controller.GUI;

import net.and0.metarogue.model.GUI.GUI;
import net.and0.metarogue.model.GUI.GUIElement;
import net.and0.metarogue.util.FileUtil;
import nu.xom.*;

import java.io.IOException;

public class GUIBuilder {

    public GUIBuilder() {
        // Auto-generated constructor
    }

    public static GUI buildGUI() {
        // Create the parser and document
        Builder builder;
        Document doc = null;

        // Load the XML file
        String file = null;
        try {
            file = FileUtil.readFile("res/gui.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Catch me some errors
        try {
            builder = new Builder();
            doc = builder.build("http://and0.net/gui.xml");
        }
        catch (ValidityException ex) {
            System.err.println("Invalid XML");
        }
        catch (ParsingException ex) {
            System.err.println("Malformed XML");
        }
        catch (IOException ex) {
            System.err.println("XML file does not exist.");
        }

        // Create the GUI to build into and eventually return
        GUI gui = new GUI();

        Element root = doc.getRootElement();
        for(int i = 0; i < root.getChildCount(); i++) {
            Node node = root.getChild(i);
            if((node instanceof Element)) gui.addElement(buildGUIElement(node));
        }

        return gui;
    }

    static GUIElement buildGUIElement(Node xmlelement) {
        // Create the element
        GUIElement guielement = new GUIElement();
        // Make the Node an Element
        Element element = (Element) xmlelement;

        // Set width and height, if specified
        if(element.getAttributeValue("height") != null) {
            guielement.height = Integer.parseInt(element.getAttributeValue("height"));
        }
        if(element.getAttributeValue("width") != null) {
            guielement.width = Integer.parseInt(element.getAttributeValue("width"));
        }
        return guielement;
    }

}