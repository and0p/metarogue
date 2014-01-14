package net.and0.metarogue.view.GUI;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import net.and0.metarogue.main.Main;
import net.and0.metarogue.model.gameobjects.GameObject;
import net.and0.metarogue.util.FileUtil;
import net.and0.metarogue.util.HexColor;
import net.and0.metarogue.util.settings.DisplaySettings;
import nu.xom.*;
import org.lwjgl.opengl.Display;

public class GUI {

    public DefaultMutableTreeNode root;
    public DefaultTreeModel gui;
    // Map of nodes which contain elements with corresponding IDs.
    Map<String, DefaultMutableTreeNode> idMap;

	public GUI(String filename) {
        GUIElement rootElement = new StaticGUIElement(0,0, DisplaySettings.resolutionX, DisplaySettings.resolutionY, 0);
		root = new DefaultMutableTreeNode(rootElement);
        //gui = new DefaultTreeModel(root);
        idMap = new HashMap<String, DefaultMutableTreeNode>();
        buildGUI(filename);
	}

    public void addElement(GUIElement e) {
        root.add(new DefaultMutableTreeNode(e));
        System.out.print(root.getChildCount() + "\n");
    }

    public void addID(String id, DefaultMutableTreeNode t) {
        idMap.put(id, t);
    }

    public void changeID(String oldID, String newID) {
        // Check if old ID exists and new ID does not
        if(idMap.containsKey(oldID) && !idMap.containsKey(newID)) {
            // Get current node of old ID
            DefaultMutableTreeNode node = idMap.get(oldID);
            // Get it's element and change it's internal ID
            GUIElement e = (GUIElement)node.getUserObject();
            e.setID(newID);
            // Swap it's parent node in the idMap from old ID to new one
            idMap.remove(oldID);
            addID(newID, node);
        }
    }

    public GUIElement getElement(String id) {
        if(idMap.containsKey(id)) return (GUIElement)idMap.get(id).getUserObject();
        return null;
    }

    public void assignGameObject(String id, GameObject go) {
        GUIElement e = (GUIElement)idMap.get(id).getUserObject();
        e.setGameObject(go);
    }

    public void assignGameObject(String id, GameObject go, String variable) {
        if(idMap.containsKey(id)) {
            GUIElement e = (GUIElement)idMap.get(id).getUserObject();
            e.setGameObject(go);
            e.setVariable(go.getVariableObject(variable));
        }
    }
	
	public Enumeration getElementsPreorder() {
        //return root.depthFirstEnumeration();
        return root.preorderEnumeration();
	}

    public void render() {
        for (Enumeration e = getElementsPreorder(); e.hasMoreElements();) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
            GUIElement g = (GUIElement)node.getUserObject();
            if(g != null && node.getLevel() > 0) {
                g.render();
            }
        }
    }

    public void buildGUI(String filename) {
        // Create the parser and document
        Builder builder;
        Document doc = null;

        // Load the XML file
        String file = null;
        try {
            file = FileUtil.readFile("res/" + filename + ".xml");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Catch me some errors
        try {
            builder = new Builder();
            doc = builder.build("http://and0.net/" + filename + ".xml");
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

        Element root = doc.getRootElement();

        this.root = processElement(root);
    }

    // Process all elements, return DefaultMutableTreeNode containing all (sub)children within this XML element
    public DefaultMutableTreeNode processElement(Element element) {
        GUIElement guielement = buildGUIElement(element);
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(guielement);
        // If an element has an ID, add it's parent TreeNode to the list of IDs in the main GUI
        if(guielement.getId() != null) {
            addID(guielement.getId(), node);
        }
        Elements children = element.getChildElements();
        for (int i = 0; i < children.size(); i++) {
            if((children.get(i) instanceof Element)) {
                node.insert(processElement(children.get(i)), i);
            }
        }
        return node;
    }

    // Oh my god this is so messy I have no idea how to user abstract classes
    static GUIElement buildGUIElement(Node xmlelement) {
        // Make the Node an Element
        Element element = (Element) xmlelement;
        // Return whatever type of element based on whether or not it's dynamic good god what am I doing
        if(element.getLocalName() == "element" || element.getLocalName() == "e") {
            // Create the element
            StaticGUIElement guielement = new StaticGUIElement();
            // Create an argument string
            String arg="";

            // Get element ID, if specified
            if(element.getAttributeValue("id") != null) guielement.id = element.getAttributeValue("id");

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
                if(arg.equals("absolute")) guielement.positiontype = GUIElement.positionType.ABSOLUTE;
                if(arg.equals("relative")) guielement.positiontype = GUIElement.positionType.RELATIVE;
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

            // Set colors for background, border, fill, etc
            if(element.getAttributeValue("bgcolor") != null) {
                guielement.backColor = HexColor.convert(element.getAttributeValue("bgcolor"));
            }
            if(element.getAttributeValue("bgc") != null) {
                guielement.backColor = HexColor.convert(element.getAttributeValue("bgc"));
            }
            if(element.getAttributeValue("bordercolor") != null) {
                guielement.borderColor = HexColor.convert(element.getAttributeValue("bordercolor"));
            }
            if(element.getAttributeValue("bc") != null) {
                guielement.borderColor = HexColor.convert(element.getAttributeValue("bc"));
            }
            if(element.getAttributeValue("fillcolor") != null) {
                guielement.fillColor = HexColor.convert(element.getAttributeValue("fillcolor"));
            }
            if(element.getAttributeValue("fc") != null) {
                guielement.fillColor = HexColor.convert(element.getAttributeValue("fc"));
            }

            // Set intended variable
            if(element.getAttributeValue("var") != null) {
                guielement.variable = element.getAttributeValue("var");
            }

            // Get text
            if(element.getAttributeValue("text") != null) {
                guielement.text = element.getAttributeValue("text");
            }

            return guielement;
        } else if (element.getLocalName() == "delement" || element.getLocalName() == "d") {
            DynamicGUIElement guielement = new DynamicGUIElement();
            // Create the element
            //StaticGUIElement guielement = new StaticGUIElement();
            // Create an argument string
            String arg="";

            // Get element ID, if specified
            if(element.getAttributeValue("id") != null) guielement.id = element.getAttributeValue("id");

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
                if(arg.equals("absolute")) guielement.positiontype = GUIElement.positionType.ABSOLUTE;
                if(arg.equals("relative")) guielement.positiontype = GUIElement.positionType.RELATIVE;
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

            // Set colors for background, border, fill, etc
            if(element.getAttributeValue("bgcolor") != null) {
                guielement.backColor = HexColor.convert(element.getAttributeValue("bgcolor"));
            }
            if(element.getAttributeValue("bgc") != null) {
                guielement.backColor = HexColor.convert(element.getAttributeValue("bgc"));
            }
            if(element.getAttributeValue("bordercolor") != null) {
                guielement.borderColor = HexColor.convert(element.getAttributeValue("bordercolor"));
            }
            if(element.getAttributeValue("bc") != null) {
                guielement.borderColor = HexColor.convert(element.getAttributeValue("bc"));
            }
            if(element.getAttributeValue("fillcolor") != null) {
                guielement.fillColor = HexColor.convert(element.getAttributeValue("fillcolor"));
            }
            if(element.getAttributeValue("fc") != null) {
                guielement.fillColor = HexColor.convert(element.getAttributeValue("fc"));
            }

            // Set intended variable
            if(element.getAttributeValue("var") != null) {
                guielement.variable = element.getAttributeValue("var");
            }

            // Get text
            if(element.getAttributeValue("text") != null) {
                guielement.text = element.getAttributeValue("text");
            }

            // Add dynamic traits if available
            guielement.setInstance(Main.dgui.root);
            guielement.objectList = Main.getActiveWorld().worldObjects;

            return guielement;
        } else {
            //TODO: Do all of the things that use these elements even know how to handle null? THINK OF THE CHILDREN (OBJECTS)!?
            return null;
        }
    }

}