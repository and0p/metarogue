package io.metarogue.game.events;

import java.util.ArrayList;

public class Queue {

    ArrayList<Event> events;

    int queueSize = 0;
    int currentEvent = 0;

    public Queue() {
        events = new ArrayList<Event>();
    }

    public Event getNext() {
        if(currentEvent <= queueSize) {
            currentEvent++;
            return events.get(currentEvent);
        }
        return null;
    }

    public void flush() {
        events.clear();
        queueSize = 0;
        currentEvent = 0;
    }

}