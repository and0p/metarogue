package net.and0.metarogue.controller;

import net.and0.metarogue.main.Main;
import net.and0.metarogue.view.GUI.GUIElement;
import net.and0.metarogue.util.threed.Vector3d;
import net.and0.metarogue.view.GUI.StaticGUIElement;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

/**
 * MetaRogue Input Parser
 * User: andrew
 * Date: 5/16/13
 * Time: 4:22 PM
 */
public class InputParser {

    static int blockType = 1;
    static int moved = 0;
    static int blockchange = 0;
    static int justmoved = 0;

    public InputParser() {
        // Stuff?
    }

    public static void parseInput() {

        if(Mouse.isButtonDown(0)) {
            if(Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)){
                if(Main.getActiveWorld().selectedBlock != null) {
                    Main.getActiveWorld().setBlock(0, Main.getActiveWorld().selectedBlock.getX(), Main.getActiveWorld().selectedBlock.getY(), Main.getActiveWorld().selectedBlock.getZ());
                }
            }
            else{
                if(Main.getActiveWorld().selectedBlock != null) {
                    Main.getActiveWorld().setBlock(7, Main.getActiveWorld().selectedBlock.getX(), Main.getActiveWorld().selectedBlock.getY()+1, Main.getActiveWorld().selectedBlock.getZ());
                }
            }
        }
        if(Mouse.isButtonDown(1)) {
            Main.getActiveWorld().getActiveCamera().rotateCamera(Mouse.getDX(), -Mouse.getDY());
        }

        int dWheel = Mouse.getDWheel();
        if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            blockType += dWheel/120;
            if(blockType < 0) blockType = 0;
            if(blockType > 254) blockType = 254;
            if(dWheel != 0) System.out.print(blockType + "\n");
        } else {
            Main.getActiveWorld().getActiveCamera().dollyCamera(-dWheel*.02);
        }


        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            Main.getActiveWorld().getActiveCamera().rotateCamera(-1,0);
            System.out.print(   Main.getActiveWorld().getActiveCamera().position.x + " " +
                    Main.getActiveWorld().getActiveCamera().position.y + " " +
                    Main.getActiveWorld().getActiveCamera().position.z + " " +
                    Main.getActiveWorld().getActiveCamera().rot[0] + " " + Main.getActiveWorld().getActiveCamera().rot[1] + "\n");
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
            Main.getRubyContainer().container.callMethod(Main.getRubyContainer().receiver, "update", Object.class);
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
            //Main.getActiveGui().addElement(new StaticGUIElement(Main.randomGenerator.nextInt(800), Main.randomGenerator.nextInt(600), 50, 50, 6));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
            //Main.getActiveGui().bullshitAddTest();
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD0)) {
            Main.getActiveGui().getElement("char").setPosition(Mouse.getX(), Display.getHeight() - Mouse.getY());
        }


        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            Display.destroy();
            System.exit(0);
        }

        // Debug GUI stuff

        if (Keyboard.isKeyDown(Keyboard.KEY_ADD)) {
            Main.getActiveWorld().playerObject.getVariableObject("health").increment(1);
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_SUBTRACT)) {
            Main.getActiveWorld().playerObject.getVariableObject("health").increment(-1);
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD2)) {
            Main.getActiveGui().getElement("char").changeHeight(1);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD8)) {
            Main.getActiveGui().getElement("char").changeHeight(-1);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD6)) {
            Main.getActiveGui().getElement("char").changeWidth(1);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD4)) {
            Main.getActiveGui().getElement("char").changeWidth(-1);
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_O)) {
            Main.getActiveGui().getElement("dynamic").changeWidth(-8);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_P)) {
            Main.getActiveGui().getElement("dynamic").changeWidth(8);
        }

        // Debug movement and block change keys.

        if (!Keyboard.isKeyDown(Keyboard.KEY_A) && !Keyboard.isKeyDown(Keyboard.KEY_S) &&
            !Keyboard.isKeyDown(Keyboard.KEY_W) && !Keyboard.isKeyDown(Keyboard.KEY_D) &&
            !Keyboard.isKeyDown(Keyboard.KEY_E) && !Keyboard.isKeyDown(Keyboard.KEY_C)) {
            moved = 0;
        }
        if (!Keyboard.isKeyDown(Keyboard.KEY_J) && !Keyboard.isKeyDown(Keyboard.KEY_K)) {
            blockchange = 0;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                Main.getActiveWorld().playerObject.move(-1,0,0);
            }
            if(moved == 0) {
                Main.getActiveWorld().playerObject.move(-1,0,0);
                justmoved = 1;
                moved = 1;
            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                Main.getActiveWorld().playerObject.move(0,0,1);
            }
            if(moved == 0) {
                Main.getActiveWorld().playerObject.move(0,0,1);
                justmoved = 1;
                moved = 1;
            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                Main.getActiveWorld().playerObject.move(0,0,-1);
            }
            if(moved == 0) {
                Main.getActiveWorld().playerObject.move(0,0,-1);
                justmoved = 1;
                moved = 1;
            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                Main.getActiveWorld().playerObject.move(1,0,0);
            }
            if(moved == 0) {
                Main.getActiveWorld().playerObject.move(1,0,0);
                justmoved = 1;
                moved = 1;
            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
            if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                Main.getActiveWorld().playerObject.move(0,1,0);
            }
            if(moved == 0) {
                Main.getActiveWorld().playerObject.move(0,1,0);
                justmoved = 1;
                moved = 1;
            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_C)) {
            if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                Main.getActiveWorld().playerObject.move(0,-1,0);
            }
            if(moved == 0) {
                Main.getActiveWorld().playerObject.move(0,-1,0);
                justmoved = 1;
                moved = 1;
            }
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_J)) {
            if(blockchange == 0 || justmoved == 1) {
                Vector3d position = Main.getActiveWorld().playerObject.getPosition();
                Main.getActiveWorld().setBlock(blockType, Main.getActiveWorld().playerObject.getPosition());
                System.out.print(position.getX() + ", " + position.getY() + ", " + position.getZ() + "\n");
                blockchange = 1;
            }
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_K)) {
            if(blockchange == 0 || justmoved == 1) {
                Main.getActiveWorld().setBlock(0, Main.getActiveWorld().playerObject.getPosition());
                blockchange = 1;
            }
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_0)) {
            blockType = 0;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_1)) {
            blockType = 1;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_2)) {
            blockType = 2;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_3)) {
            blockType = 3;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_4)) {
            blockType = 4;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_5)) {
            blockType = 5;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_6)) {
            blockType = 6;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_7)) {
            blockType = 7;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_8)) {
            blockType = 8;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_9)) {
            blockType = 9;
        }

        justmoved = 0;


    }

}
