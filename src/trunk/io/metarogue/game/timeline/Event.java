package io.metarogue.game.timeline;

import io.metarogue.util.Log;
import io.metarogue.util.Timer;
import io.metarogue.game.gamemessage.GameMessage;

import java.util.ArrayList;

public class Event extends StoryComposite implements Update {

    ArrayList<GameMessage> gameMessages;
    static final Event blankEvent = new Event();

    public Event() {
        gameMessages = new ArrayList<GameMessage>();
    }

    public Event(GameMessage a) {
        gameMessages = new ArrayList<GameMessage>();
        gameMessages.add(a);
    }

    public void run() {
        if(Log.logging) {
            Log.log("    Running event " + id + " at " + Timer.getMilliTime());
        }
        super.run();
    }

    public void reverse() {
        if(Log.logging) {
            Log.log("    Reversing event " + id + " at " + Timer.getMilliTime());
        }
        super.reverse();
    }

    public StoryComponent getStoryComponent(int i) {
        if(i >= 0 && i < getSize()) {
            return gameMessages.get(i);
        }
        Log.log("ERROR: StoryObject index out of bounds bro~");
        // return BlankMessage.getInstance();
        return null;
    }

    public GameMessage getFirstMessage() {
        if(getSize() > 0) {
            return getMessage(0);
        }
        //return BlankMessage.getInstance();
        return null;
    }

    public GameMessage getMessage(int i) {
        if (!gameMessages.isEmpty() && i < gameMessages.size() && i>= 0) {
            return gameMessages.get(i);
        }
        Log.log("ERROR: Action index out of bounds bro~");
        // return BlankMessage.getInstance();
        return null;
    }

    public void updateAnimation(float progress) {}
    public void finishAnimation() {}
    public void revertAnimation() {}

    public void addMessage(GameMessage a) {
        gameMessages.add(a);
    }

    public int getSize() {
        return gameMessages.size();
    }

    public static Event getInstance() { return blankEvent; }

}