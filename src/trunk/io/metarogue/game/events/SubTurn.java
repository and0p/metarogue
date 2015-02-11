package io.metarogue.game.events;

import io.metarogue.util.Log;
import io.metarogue.util.Timer;

import java.util.ArrayList;

public class SubTurn extends StoryComposite {

    ArrayList<Event> events;

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

    public Event getEvent(int i) {
        return events.get(i);
    }

    public void addEvent(Event e) {
        events.add(e);
    }

    public int getSize() {
        return events.size();
    }


}