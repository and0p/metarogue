package io.metarogue.client.listener;

import io.metarogue.util.messagesystem.Listener;
import io.metarogue.util.messagesystem.message.Message;
import io.metarogue.util.messagesystem.message.chat.ChatMessage;
import io.metarogue.util.messagesystem.message.game.player.PlayerAssignment;

public class ClientListener implements Listener {

    public void receive(Message m) {
        if(m instanceof ChatMessage) {
            // Grab sender info, add message to chat box
        }
        if(m instanceof PlayerAssignment) {
            m.run();
        }
    }

}
