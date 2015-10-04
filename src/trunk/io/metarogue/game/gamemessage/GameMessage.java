package io.metarogue.game.gamemessage;

import io.metarogue.game.timeline.StoryComponent;
import io.metarogue.util.messagesystem.message.Message;

public abstract class GameMessage implements Message, StoryComponent {

    int sender = -1;
    boolean tcp = false;

    public GameMessage() {
        // Auto-generated constructor
    }

    public abstract void run();
    public abstract void reverse();

    public void setSender(int i) {
        sender = i;
    }

    public int getSender() {
        return sender;
    }

    // TODO: These shouldn't default to true in production, make abstract?
    public boolean sanitize() { return true; }
    public boolean verify() { return true; }

    public boolean isTCP() {
        return true;
    }

}
