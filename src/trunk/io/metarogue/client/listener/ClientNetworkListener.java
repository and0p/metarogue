package io.metarogue.client.listener;

import io.metarogue.Main;
import io.metarogue.util.messagesystem.Listener;
import io.metarogue.util.messagesystem.message.Message;
import io.metarogue.util.messagesystem.message.chat.ChatMessage;

public class ClientNetworkListener implements Listener{

    public void receive(Message m) {
        if(m instanceof ChatMessage) {
            ChatMessage message = (ChatMessage)m;
            if((Main.getClient().getClientID() != 0) && (message.getSender() == Main.getClient().getClientID())) {
                Main.getClient().sendMessage(message);
            }
        }
    }

}