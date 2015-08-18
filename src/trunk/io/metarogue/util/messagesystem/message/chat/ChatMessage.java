package io.metarogue.util.messagesystem.message.chat;

import io.metarogue.game.Player;
import io.metarogue.util.messagesystem.message.MessageImpl;

public class ChatMessage extends MessageImpl {

    Player target;
    int group;
    String text;

    public ChatMessage(){}

    public ChatMessage(int group, String text) {
        this.group = group;
        this.text = text;
    }

    public ChatMessage(Player target, String text) {
        this.target = target;
        this.text = text;
    }

    public Player getPlayer() {
        return target;
    }

    public boolean isTCP() {
        return true;
    }

    public boolean sanitize() {
        //TODO: Sanitize inputs
        return true;
    }

}