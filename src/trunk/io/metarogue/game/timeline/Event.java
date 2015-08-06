package io.metarogue.game.timeline;

import io.metarogue.game.timeline.actions.Action;
import io.metarogue.util.Log;
import io.metarogue.util.Timer;

import java.util.ArrayList;

public class Event extends StoryComposite implements Update {

    ArrayList<Action> actions;
    static final Event blankEvent = new Event();

    public Event() {
        actions = new ArrayList<Action>();
    }

    public Event(Action a) {
        actions = new ArrayList<Action>();
        actions.add(a);
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
        // return BlankAction.getInstance();
        return null;
    }

    public Action getFirstAction() {
        if(getSize() > 0) {
            return getAction(0);
        }
        //return BlankAction.getInstance();
        return null;
    }

    public Action getAction(int i) {
        if (!actions.isEmpty() && i < actions.size() && i>= 0) {
            return actions.get(i);
        }
        Log.log("ERROR: Action index out of bounds bro~");
        // return BlankAction.getInstance();
        return null;
    }

    public void updateAnimation(float progress) {}
    public void finishAnimation() {}
    public void revertAnimation() {}

    public void addAction(Action a) {
        actions.add(a);
    }

    public int getSize() {
        return actions.size();
    }

    public static Event getInstance() { return blankEvent; }

}