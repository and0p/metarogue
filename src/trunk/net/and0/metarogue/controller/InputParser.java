package net.and0.metarogue.controller;

import net.and0.metarogue.main.GameClient;
import net.and0.metarogue.main.Main;
import net.and0.metarogue.model.Camera;
import net.and0.metarogue.util.threed.Vector3d;
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

    GameClient client;

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
                if(Main.gameClient.selectedBlock != null) {
                    Main.gameClient.getActiveWorld().setBlock(0, Main.gameClient.selectedBlock.getX(), Main.gameClient.selectedBlock.getY(), Main.gameClient.selectedBlock.getZ());
                }
            }
            else{
                if(Main.gameClient.selectedBlock != null) {
                    Main.gameClient.getActiveWorld().setBlock(7, Main.gameClient.selectedBlock.getX(), Main.gameClient.selectedBlock.getY()+1, Main.gameClient.selectedBlock.getZ());
                }
            }
        }
        if(Mouse.isButtonDown(1)) {
            Main.gameClient.getCurrentCamera().rotateCamera(Mouse.getDX(), -Mouse.getDY());
        }

        int dWheel = Mouse.getDWheel();
        if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            blockType += dWheel/120;
            if(blockType < 0) blockType = 0;
            if(blockType > 254) blockType = 254;
            if(dWheel != 0) System.out.print(blockType + "\n");
        } else {
            Main.gameClient.getCurrentCamera().dollyCamera(-dWheel * .02);
        }


        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            Camera c = Main.gameClient.getCurrentCamera();
            c.rotateCamera(-1,0);
            System.out.print(   c.position.x + " " + c.position.y + " " + c.position.z + " " + c.rot[0] + " " + c.rot[1] + "\n");
        }

//        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
//            Main.getRubyContainer().container.callMethod(Main.getRubyContainer().receiver, "update", Object.class);
//        }

//        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
//            //Main.getBaseGUI().addElement(new StaticGUIElement(Main.randomGenerator.nextInt(800), Main.randomGenerator.nextInt(600), 50, 50, 6));
//        }
//        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
//            //Main.getBaseGUI().bullshitAddTest();
//        }
//
//        if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD0)) {
//            Main.getBaseGUI().getElement("char").setPosition(Mouse.getX(), Display.getHeight() - Mouse.getY());
//        }


        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            Display.destroy();
            System.exit(0);
        }

        // Debug GUI stuff

//        if (Keyboard.isKeyDown(Keyboard.KEY_ADD)) {
//           Main.gameClient.getPlayer().getVariableObject("health").increment(1);
//        }
//
//        if (Keyboard.isKeyDown(Keyboard.KEY_SUBTRACT)) {
//           Main.gameClient.getPlayer().getVariableObject("health").increment(-1);
//        }
//
//        if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD2)) {
//            Main.getBaseGUI().getElement("char").changeHeight(5);
//        }
//        if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD8)) {
//            Main.getBaseGUI().getElement("char").changeHeight(-5);
//        }
//        if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD6)) {
//            Main.getBaseGUI().getElement("char").changeWidth(5);
//        }
//        if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD4)) {
//            Main.getBaseGUI().getElement("char").changeWidth(-5);
//        }
//
//        if (Keyboard.isKeyDown(Keyboard.KEY_O)) {
//            Main.getBaseGUI().getElement("dynamic").changeWidth(-8);
//        }
//        if (Keyboard.isKeyDown(Keyboard.KEY_P)) {
//            Main.getBaseGUI().getElement("dynamic").changeWidth(8);
//        }

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
                Main.gameClient.getPlayer().move(-1,0,0);
            }
            if(moved == 0) {
                Main.gameClient.getPlayer().move(-1,0,0);
                justmoved = 1;
                moved = 1;
            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
               Main.gameClient.getPlayer().move(0,0,1);
            }
            if(moved == 0) {
               Main.gameClient.getPlayer().move(0,0,1);
                justmoved = 1;
                moved = 1;
            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
               Main.gameClient.getPlayer().move(0,0,-1);
            }
            if(moved == 0) {
               Main.gameClient.getPlayer().move(0,0,-1);
                justmoved = 1;
                moved = 1;
            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
               Main.gameClient.getPlayer().move(1,0,0);
            }
            if(moved == 0) {
               Main.gameClient.getPlayer().move(1,0,0);
                justmoved = 1;
                moved = 1;
            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
            if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
               Main.gameClient.getPlayer().move(0,1,0);
            }
            if(moved == 0) {
               Main.gameClient.getPlayer().move(0,1,0);
                justmoved = 1;
                moved = 1;
            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_C)) {
            if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
               Main.gameClient.getPlayer().move(0,-1,0);
            }
            if(moved == 0) {
               Main.gameClient.getPlayer().move(0,-1,0);
                justmoved = 1;
                moved = 1;
            }
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_J)) {
            if(blockchange == 0 || justmoved == 1) {
                Vector3d position =Main.gameClient.getPlayer().getPosition();
               Main.gameClient.getActiveWorld().setBlock(blockType,Main.gameClient.getPlayer().getPosition());
                System.out.print(position.getX() + ", " + position.getY() + ", " + position.getZ() + "\n");
                blockchange = 1;
            }
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_K)) {
            if(blockchange == 0 || justmoved == 1) {
               Main.gameClient.getActiveWorld().setBlock(0,Main.gameClient.getPlayer().getPosition());
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



//        if (Keyboard.isKeyDown(Keyboard.KEY_B)) {
//            Main.server.start();
//        }
//        if (Keyboard.isKeyDown(Keyboard.KEY_N)) {
//            Main.gameClient.startNetworking();
//            Main.gameClient.connect("127.0.0.1", 54555, 54777);
//        }
//        if (Keyboard.isKeyDown(Keyboard.KEY_M)) {
//            TextMessage request = new TextMessage();
//            request.text = "Here is the request";
//            Main.gameClient.message();
//        }


    }

}