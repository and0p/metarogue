package io.metarogue.client.listener;

import io.metarogue.game.gamemessage.GameMessage;
import io.metarogue.util.messagesystem.Listener;
import io.metarogue.util.messagesystem.message.Message;
import io.metarogue.util.messagesystem.message.chat.ChatMessage;
import io.metarogue.game.gamemessage.player.PlayerAssignment;
import io.metarogue.game.gamemessage.player.PlayerSkeleton;
import io.metarogue.util.messagesystem.message.skeleton.GameSkeleton;

public class ClientListener implements Listener {

    public void receive(Message m) {
        if(m instanceof GameMessage) {

        }
        if(m instanceof ChatMessage) {
            // Grab sender info, add message to chat box
        }
        if(m instanceof GameSkeleton) {
            GameSkeleton gs = (GameSkeleton)m;
            gs.run();
        }
        if(m instanceof PlayerSkeleton) {
            PlayerSkeleton ps = (PlayerSkeleton)m;
            ps.run();
        }
        if(m instanceof PlayerAssignment) {
            PlayerAssignment pa = (PlayerAssignment)m;
            pa.run();
        }
    }

}