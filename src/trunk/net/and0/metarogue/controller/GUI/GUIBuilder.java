package net.and0.metarogue.controller.GUI;

import net.and0.metarogue.view.GUI.GUI;
import net.and0.metarogue.view.GUI.GUIElement;
import net.and0.metarogue.util.FileUtil;
import net.and0.metarogue.view.GUI.StaticGUIElement;
import nu.xom.*;

import javax.swing.tree.DefaultMutableTreeNode;
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
        GUI gui = new GUI("gui");

        Element root = doc.getRootElement();
        //processElement(root);
        gui.root = processElement(root);
//        for(int i = 0; i < root.getChildCount(); i++) {
//            Node node = root.getChild(i);
//            if((node instanceof Element)) gui.addElement(buildGUIElement(node));
//        }

        return gui;
    }

    // Process all elements, return DefaultMutableTreeNode containing all (sub)children within this XML element
    public static DefaultMutableTreeNode processElement(Element element) {

        GUIElement guielement = buildGUIElement(element);

        DefaultMutableTreeNode node = new DefaultMutableTreeNode(guielement);
        Elements children = element.getChildElements();
        for (int i = 0; i < children.size(); i++) {
            if((children.get(i) instanceof Element)) node.insert(processElement(children.get(i)), i);
        }

        return node;

    }

    static GUIElement buildGUIElement(Node xmlelement) {
        // Create the element
        GUIElement guielement = new StaticGUIElement();
        // Make the Node an Element
        Element element = (Element) xmlelement;
        // Create an argument string
        String arg="";

        // Set width and height, if specified
        if(element.getAttributeValue("height") != null) guielement.height = Integer.parseInt(element.getAttributeValue("height"));
        if(element.getAttributeValue("width") != null) guielement.width = Integer.parseInt(element.getAttributeValue("width"));
        if(element.getAttributeValue("h") != null) guielement.height = Integer.parseInt(element.getAttributeValue("h"));
        if(element.getAttributeValue("w") != null) guielement.width = Integer.parseInt(element.getAttributeValue("w"));

        // Set position if specified
        if(element.getAttributeValue("x") != null) guielement.position.setX(Integer.parseInt(element.getAttributeValue("x")));
        if(element.getAttributeValue("y") != null) guielement.position.setY(Integer.parseInt(element.getAttributeValue("y")));

        // Set alignment if specified
        if(element.getAttributeValue("halign") != null) {
            arg = element.getAttributeValue("halign").toLowerCase();
            if(arg.equals("left")) guielement.halign = GUIElement.hAlign.LEFT;
            else if(arg.equals("center")) guielement.halign = GUIElement.hAlign.CENTER;
            else if(arg.equals("right")) guielement.halign = GUIElement.hAlign.RIGHT;
        }
        if(element.getAttributeValue("valign") != null) {
            arg = element.getAttributeValue("valign").toLowerCase();
            if(arg.equals("top")) guielement.valign = GUIElement.vAlign.TOP;
            else if(arg.equals("center")) guielement.valign = GUIElement.vAlign.CENTER;
            else if(arg.equals("bottom")) guielement.valign = GUIElement.vAlign.BOTTOM;
        }

        // Get position type
        if(element.getAttributeValue("positiontype") != null) {
            arg = element.getAttributeValue("positiontype").toLowerCase();
            if(arg.equals("absolute")) guielement.absolutePosition = true;
            if(arg.equals("relative")) guielement.absolutePosition = false;
        }
        if(element.getAttributeValue("ptype") != null) {
            arg = element.getAttributeValue("ptype").toLowerCase();
            if(arg.equals("absolute")) guielement.absolutePosition = true;
            if(arg.equals("relative")) guielement.absolutePosition = false;
        }

        // Get margin(s)
        if(element.getAttributeValue("margin") != null) {
            arg = element.getAttributeValue("margin").toLowerCase();
            arg.replaceAll("\\s",""); // Replace all whitespace
            String[] argsplit = arg.split(",");
            if(argsplit.length == 1) {
                guielement.margin[0] = Integer.parseInt(argsplit[0]);
                guielement.margin[1] = Integer.parseInt(argsplit[0]);
                guielement.margin[2] = Integer.parseInt(argsplit[0]);
                guielement.margin[3] = Integer.parseInt(argsplit[0]);
            }
            else if(argsplit.length == 2) {
                guielement.margin[0] = Integer.parseInt(argsplit[0]);
                guielement.margin[1] = Integer.parseInt(argsplit[1]);
                guielement.margin[2] = Integer.parseInt(argsplit[0]);
                guielement.margin[3] = Integer.parseInt(argsplit[1]);
            }
            else if(argsplit.length == 4) {
                guielement.margin[0] = Integer.parseInt(argsplit[0]);
                guielement.margin[1] = Integer.parseInt(argsplit[1]);
                guielement.margin[2] = Integer.parseInt(argsplit[2]);
                guielement.margin[3] = Integer.parseInt(argsplit[3]);
            }
        }
        if(element.getAttributeValue("m") != null) {
            arg = element.getAttributeValue("m").toLowerCase();
            arg.replaceAll("\\s",""); // Replace all whitespace
            String[] argsplit = arg.split(",");
            if(argsplit.length == 1) {
                guielement.margin[0] = Integer.parseInt(argsplit[0]);
                guielement.margin[1] = Integer.parseInt(argsplit[0]);
                guielement.margin[2] = Integer.parseInt(argsplit[0]);
                guielement.margin[3] = Integer.parseInt(argsplit[0]);
            }
            else if(argsplit.length == 2) {
                guielement.margin[0] = Integer.parseInt(argsplit[0]);
                guielement.margin[1] = Integer.parseInt(argsplit[1]);
                guielement.margin[2] = Integer.parseInt(argsplit[0]);
                guielement.margin[3] = Integer.parseInt(argsplit[1]);
            }
            else if(argsplit.length == 4) {
                guielement.margin[0] = Integer.parseInt(argsplit[0]);
                guielement.margin[1] = Integer.parseInt(argsplit[1]);
                guielement.margin[2] = Integer.parseInt(argsplit[2]);
                guielement.margin[3] = Integer.parseInt(argsplit[3]);
            }
        }

        // Get text
//        if(element.getValue() != null) {
//            guielement.text = element.getValue();
//        }

        return guielement;
    }

}