package net.and0.metarogue.controller;

import net.and0.metarogue.main.Main;
import net.and0.metarogue.model.GUI.GUIElement;
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
        Main.getActiveWorld().getActiveCamera().dollyCamera(-Mouse.getDWheel()*.02);

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
            Main.getActiveGui().addElement(new GUIElement(Main.randomGenerator.nextInt(800), Main.randomGenerator.nextInt(600), 50, 50, 6));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
            Main.getActiveGui().bullshitAddTest();
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_0)) {
            Main.getActiveGui().bullshitGetKid().setPosition(Mouse.getX(), Display.getHeight() - Mouse.getY());
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            Display.destroy();
            System.exit(0);
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            Main.getActiveWorld().playerObject.move(-1,0,0);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            Main.getActiveWorld().playerObject.move(0,0,1);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            Main.getActiveWorld().playerObject.move(0,0,-1);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            Main.getActiveWorld().playerObject.move(1,0,0);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
            Main.getActiveWorld().playerObject.move(0,1,0);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_C)) {
            Main.getActiveWorld().playerObject.move(0,-1,0);
        }

    }

}
