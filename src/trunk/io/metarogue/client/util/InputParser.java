package io.metarogue.client.util;

import io.metarogue.client.GameClient;
import io.metarogue.Main;
import io.metarogue.game.Camera;
import io.metarogue.client.view.threed.Vector3d;
import io.metarogue.game.events.Event;
import io.metarogue.game.events.time.Timestamp;
import io.metarogue.game.events.actions.Action;
import io.metarogue.game.events.actions.BlockAction;
import io.metarogue.game.events.actions.RelativeMoveAction;
import io.metarogue.game.gameobjects.GameObject;
import io.metarogue.util.Log;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import java.util.ArrayList;

/**
 * MetaRogue Input Parser
 * User: andrew
 * Date: 5/16/13
 * time: 4:22 PM
 */
public class InputParser {

    static int blockType = 1;
    static int moved = 0;
    static int blockchange = 0;
    static int justmoved = 0;
    static int testing = 0;
    static boolean run = false;

    public InputParser() {
        // Stuff?
    }

    public static void parseInput() {

        testing = 0;

        if(Mouse.isButtonDown(0)) {
            if(Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)){
                if(Main.getClient().selectedBlock != null) {
                    Main.getClient().getActiveWorld().setBlock(0, Main.getClient().selectedBlock.getX(), Main.getClient().selectedBlock.getY(), Main.getClient().selectedBlock.getZ());
                }
            }
            else{
                if(Main.getClient().selectedBlock != null) {
                    Main.getClient().getActiveWorld().setBlock(7, Main.getClient().selectedBlock.getX(), Main.getClient().selectedBlock.getY()+1, Main.getClient().selectedBlock.getZ());
                }
            }
        }
        if(Mouse.isButtonDown(1)) {
            Main.getClient().getCurrentCamera().rotateCamera(Mouse.getDX()/4f, -Mouse.getDY()/4f);
        }

        int dWheel = Mouse.getDWheel();
        if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            blockType += dWheel/120;
            if(blockType < 0) blockType = 0;
            if(blockType > 254) blockType = 254;
            if(dWheel != 0) Log.log(blockType + "\n");
        } else {
            Main.getClient().getCurrentCamera().dollyCamera(-dWheel * .02);
        }


        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            Camera c = Main.getClient().getCurrentCamera();
            c.rotateCamera(-1,0);
            //System.out.print(   c.position.x + " " + c.position.y + " " + c.position.z + " " + c.rot[0] + " " + c.rot[1] + "\n");
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_L)) {
            Event e = new Event();
            RelativeMoveAction a = new RelativeMoveAction(Main.getClient().getPlayer(),-1, 0, 0);
            e.addAction(a);
            Main.getGame().getStory().addEventAndEndSubturn(e);
            ArrayList<Event> eventList = new ArrayList<Event>();
            for(GameObject go : Main.getGame().getGameObjects()) {
                Vector3d v3d = new Vector3d((int)(Math.random()*3)-1,0,(int)(Math.random()*3)-1);
                Event i = new Event(new RelativeMoveAction(go, v3d));
                eventList.add(i);
            }
            Main.getGame().getStory().addEvents(eventList);
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

        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            int i = 0;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            Display.destroy();
            System.exit(0);
        }

        // Debug GUI stuff

//        if (Keyboard.isKeyDown(Keyboard.KEY_ADD)) {
//           Main.getClient().getPlayer().getVariableObject("health").increment(1);
//        }
//
//        if (Keyboard.isKeyDown(Keyboard.KEY_SUBTRACT)) {
//           Main.getClient().getPlayer().getVariableObject("health").increment(-1);
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

        // Debug story keys

        if(Keyboard.isKeyDown(Keyboard.KEY_NUMPAD6) || Keyboard.isKeyDown(Keyboard.KEY_APOSTROPHE)) {
            Timestamp t = Main.getGame().getStory().getDisplayStamp().copy();
            t.changeTurn(1);
            Main.getGame().getStory().track(t);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_NUMPAD4) || Keyboard.isKeyDown(Keyboard.KEY_SEMICOLON)) {
            Timestamp t = Main.getGame().getStory().getDisplayStamp().copy();
            t.changeTurn(-1);
            Main.getGame().getStory().track(new Timestamp(0,0,0,0));
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_NUMPAD8) || Keyboard.isKeyDown((Keyboard.KEY_RBRACKET))) {
            Main.getGame().getStory().play();
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_NUMPAD5) || Keyboard.isKeyDown((Keyboard.KEY_LBRACKET))) {
            Main.getGame().getStory().pause();
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
                Event e = new Event();
                RelativeMoveAction a = new RelativeMoveAction(Main.getClient().getPlayer(),-1, 0, 0);
                e.addAction(a);
                Main.getGame().getStory().addEvent(e);
            }
            if(moved == 0) {
                Event e = new Event();
                RelativeMoveAction a = new RelativeMoveAction(Main.getClient().getPlayer(),-1, 0, 0);
                e.addAction(a);
                Main.getGame().getStory().addEvent(e);
                justmoved = 1;
                moved = 1;
            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                Event e = new Event();
                RelativeMoveAction a = new RelativeMoveAction(Main.getClient().getPlayer(),0, 0, 1);
                e.addAction(a);
                Main.getGame().getStory().addEvent(e);
            }
            if(moved == 0) {
                Event e = new Event();
                RelativeMoveAction a = new RelativeMoveAction(Main.getClient().getPlayer(),0, 0, 1);
                e.addAction(a);
                Main.getGame().getStory().addEvent(e);
                justmoved = 1;
                moved = 1;
            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                Event e = new Event();
                RelativeMoveAction a = new RelativeMoveAction(Main.getClient().getPlayer(),0, 0, 1);
                e.addAction(a);
                Main.getGame().getStory().addEvent(e);
            }
            if(moved == 0) {
                Event e = new Event();
                RelativeMoveAction a = new RelativeMoveAction(Main.getClient().getPlayer(),0, 0, -1);
                e.addAction(a);
                Main.getGame().getStory().addEvent(e);
                justmoved = 1;
                moved = 1;
            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                Event e = new Event();
                RelativeMoveAction a = new RelativeMoveAction(Main.getClient().getPlayer(),1, 0, 0);
                e.addAction(a);
                Main.getGame().getStory().addEvent(e);
            }
            if(moved == 0) {
                Event e = new Event();
                RelativeMoveAction a = new RelativeMoveAction(Main.getClient().getPlayer(),1, 0, 0);
                e.addAction(a);
                Main.getGame().getStory().addEvent(e);
                justmoved = 1;
                moved = 1;
            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
            if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                Event e = new Event();
                RelativeMoveAction a = new RelativeMoveAction(Main.getClient().getPlayer(),0, 1, 0);
                e.addAction(a);
                Main.getGame().getStory().addEvent(e);
            }
            if(moved == 0) {
                Event e = new Event();
                RelativeMoveAction a = new RelativeMoveAction(Main.getClient().getPlayer(),0, 1, 0);
                e.addAction(a);
                Main.getGame().getStory().addEvent(e);
                justmoved = 1;
                moved = 1;
            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_C)) {
            if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                Event e = new Event();
                RelativeMoveAction a = new RelativeMoveAction(Main.getClient().getPlayer(),0, -1, 0);
                e.addAction(a);
                Main.getGame().getStory().addEvent(e);
            }
            if(moved == 0) {
                Event e = new Event();
                RelativeMoveAction a = new RelativeMoveAction(Main.getClient().getPlayer(),0, -1, 0);
                e.addAction(a);
                Main.getGame().getStory().addEvent(e);
                justmoved = 1;
                moved = 1;
            }
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_J)) {
            if(blockchange == 0 || justmoved == 1) {
                Vector3d position =Main.getClient().getPlayer().getPosition();
                Event e = new Event();
                BlockAction a = new BlockAction(Main.getClient().getActiveWorld().id, position, blockType);
                e.addAction(a);
                Main.getGame().getStory().addEvent(e);
                blockchange = 1;
            }
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_K)) {
            if(blockchange == 0 || justmoved == 1) {
                Vector3d position =Main.getClient().getPlayer().getPosition();
                Event e = new Event();
                BlockAction a = new BlockAction(Main.getClient().getActiveWorld().id, position, 0);
                e.addAction(a);
                Main.getGame().getStory().addEvent(e);
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

        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            int i = 1;
        }





//        if (Keyboard.isKeyDown(Keyboard.KEY_B)) {
//            Main.server.start();
//        }
//        if (Keyboard.isKeyDown(Keyboard.KEY_N)) {
//            Main.getClient().startNetworking();
//            Main.getClient().connect("127.0.0.1", 54555, 54777);
//        }
//        if (Keyboard.isKeyDown(Keyboard.KEY_M)) {
//            TextMessage request = new TextMessage();
//            request.text = "Here is the request";
//            Main.getClient().message();
//        }


    }

}