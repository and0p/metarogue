package io.metarogue.game.events;

import java.util.ArrayList;

public class SubTurn {

    int animationTime;

    ArrayList<Event> events;

    public SubTurn() {
        events = new ArrayList<Event>();
    }

    public SubTurn(ArrayList<Event> events) {
        this.events = new ArrayList<Event>();
    }

    public int getAnimationTime() {
        //TODO: Add up animations
        return 0;
    }

    public void validate() {
        //TODO: If unit was destroyed in previous subturn, remove action
    }

    public void run() {
        for(Event e : events) {
            e.runAll();
        }
    }

}