package io.metarogue.server.listener;

import io.metarogue.Main;
import io.metarogue.game.Player;
import io.metarogue.server.user.User;
import io.metarogue.util.Log;
import io.metarogue.util.messagesystem.Listener;
import io.metarogue.util.messagesystem.message.Message;
import io.metarogue.util.messagesystem.message.chat.ChatMessage;
import io.metarogue.util.messagesystem.message.connection.RegistrationMessage;
import io.metarogue.game.gamemessage.player.PlayerAssignment;
import io.metarogue.util.messagesystem.type.MetaMessage;

public class ServerListener implements Listener {

    public void receive(Message m) {
        // See if this is something the server cares about on a meta-game level
        if(m instanceof MetaMessage) {
            if(m.verify()) {
                // See if it is legit at all, if so check type and act accordingly.
                if(m instanceof RegistrationMessage) {
                    RegistrationMessage message = (RegistrationMessage)m;
                    User u = Main.getServer().getUser(message.getSender());
                    u.register(message.getNick());
                    Player p = u.createPlayer();
                    Main.getServer().sendMessage(u.getID(), Main.getGame().getSkeleton());
                    Main.getServer().sendMessageToAll(p.getSkeleton());
                    Main.getServer().sendMessage(u.getID(), new PlayerAssignment(u.getID()));
                }
                if(m instanceof ChatMessage) {
                    Log.log("Recieved chat message yayyy!");
                    ChatMessage message = (ChatMessage)m;
                    if(message.getPlayer() != null) {
                        // Get User from player
                        // Send raw message to that user connection
                    } else {
                        // Send messages to specific channel
                    }
                }
            }
        }
    }
}