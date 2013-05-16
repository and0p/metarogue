package net.and0.metarogue.controller;

import net.and0.metarogue.main.Main;
import net.and0.metarogue.model.GUI.Element;
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

        if(Mouse.isButtonDown(1)) {
            Main.renderer.camera.rotateCamera(Mouse.getDX(), -Mouse.getDY());
        }
        Main.renderer.camera.dollyCamera(-Mouse.getDWheel()*.02);

        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            Main.renderer.camera.rotateCamera(-1,0);
            System.out.print( 	Main.renderer.camera.position.x + " " +
                    Main.renderer.camera.position.y + " " +
                    Main.renderer.camera.position.z + " " +
                    Main.renderer.camera.rot[0] + " " + Main.renderer.camera.rot[1] + "\n");
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
            Main.getRubyContainer().container.callMethod(Main.getRubyContainer().receiver, "update", Object.class);
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
            Main.getActiveGui().elements.add(new Element(Main.randomGenerator.nextInt(800), Main.randomGenerator.nextInt(600), 50, 50, 6));
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            Display.destroy();
            System.exit(0);
        }

    }

}
