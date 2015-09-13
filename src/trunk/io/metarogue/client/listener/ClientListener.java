package io.metarogue.client.listener;

import io.metarogue.util.messagesystem.Listener;
import io.metarogue.util.messagesystem.message.Message;
import io.metarogue.util.messagesystem.message.chat.ChatMessage;
import io.metarogue.util.messagesystem.message.game.player.PlayerAssignment;
import io.metarogue.util.messagesystem.message.game.player.PlayerSkeleton;
import io.metarogue.util.messagesystem.message.skeleton.GameSkeleton;

public class ClientListener implements Listener {

    public void receive(Message m) {
        if(m instanceof ChatMessage) {
            // Grab sender info, add message to chat box
        }
        if(m instanceof GameSkeleton) {
            m.run();
        }
        if(m instanceof PlayerSkeleton) {
            m.run();
        }
        if(m instanceof PlayerAssignment) {
            m.run();
        }
    }

}
