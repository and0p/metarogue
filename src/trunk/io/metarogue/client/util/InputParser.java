package io.metarogue.client.util;

import io.metarogue.Main;
import io.metarogue.util.Log;
import io.metarogue.util.messagesystem.message.chat.ChatMessage;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

/**
 * MetaRogue Input Parser
 * User: andrew
 * Date: 5/16/13
 * time: 4:22 PM
 */
public class InputParser {

    public static void parseInput() {

//        if(Mouse.isButtonDown(1)) {
//            Main.getClient().getCurrentCamera().rotateCamera(Mouse.getDX()/4f, -Mouse.getDY()/4f);
//        }

//        int dWheel = Mouse.getDWheel();
//        if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
//            blockType += dWheel/120;
//            if(blockType < 0) blockType = 0;
//            if(blockType > 254) blockType = 254;
//            if(dWheel != 0) Log.log(blockType + "\n");
//        } else {
//            Main.getClient().getCurrentCamera().dollyCamera(-dWheel * .02);
//        }

        if (Keyboard.isKeyDown(Keyboard.KEY_T)) {
            Log.log("Sending text message?");
            ChatMessage m = new ChatMessage(0, "Hi hi hi");
            Main.getClient().addMessage(m);
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            Display.destroy();
            System.exit(0);
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            int i = 1;
        }
    }

}