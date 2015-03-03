package io.metarogue.game.events;

import io.metarogue.game.events.actions.Action;
import io.metarogue.game.events.actions.BlankAction;
import io.metarogue.game.gameobjects.GameObject;
import io.metarogue.util.Log;
import io.metarogue.util.Timer;

import java.util.ArrayList;

public class Event extends StoryComposite {

    ArrayList<Action> actions;
    static final Event blankEvent = new Event();

    public Event() {
        actions = new ArrayList<Action>();
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
            return actions.get(i);
        }
        Log.log("ERROR: StoryObject index out of bounds bro~");
        return BlankAction.getInstance();
    }

    public Action getFirstAction() {
        if(getSize() > 0) {
            return getAction(0);
        }
        return BlankAction.getInstance();
    }

    public Action getAction(int i) {
        if(i >= 0 && i < getSize()) {
            return actions.get(i);
        }
        Log.log("ERROR: Action index out of bounds bro~");
        return BlankAction.getInstance();
    }

    public void addAction(Action a) {
        actions.add(a);
    }

    public int getSize() {
        return actions.size();
    }

    public static Event getInstance() { return blankEvent; }

}