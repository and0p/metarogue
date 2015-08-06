package io.metarogue.game.timeline;

import io.metarogue.game.timeline.actions.Action;
import io.metarogue.util.Log;
import io.metarogue.util.Timer;

import java.util.ArrayList;

public class SubTurn extends StoryComposite implements Update {

    ArrayList<Event> events;

    public SubTurn() {
        events = new ArrayList<Event>();
    }

    public void run() {
        if(Log.logging) {
            Log.log("  Running subturn " + id + " at " + Timer.getMilliTime());
        }
        super.run();
    }

    public void reverse() {
        if(Log.logging) {
            Log.log("  Reversing subturn " + id + " at " + Timer.getMilliTime());
        }
        super.reverse();
    }

    public StoryComponent getStoryComponent(int i) {
        if(i >= 0 && i < getSize()) {
            return events.get(i);
        }
        Log.log("ERROR: StoryObject index out of bounds bro~");
        return null;
    }

    public Action getFirstAction() {
        if(getSize() > 0) {
            return getEvent(0).getFirstAction();
        }
        return null;
    }

    public Event getEvent(int i) {
        if (!events.isEmpty() && i < events.size() && i>= 0) {
            return events.get(i);
        }
        Log.log("ERROR: Event index out of bounds bro~");
        return null;
    }

    public void updateAnimation(float progress) {}
    public void finishAnimation() {}
    public void revertAnimation() {}

    public void addEvent(Event e) {
        events.add(e);
    }

    public int getSize() {
        return events.size();
    }


}