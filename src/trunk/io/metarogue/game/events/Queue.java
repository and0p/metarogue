package io.metarogue.game.events;

import java.util.ArrayList;

public class Queue {

    ArrayList<Event> events;
    int queueSize;
    int currentEvent;

    public Queue() {
        events = new ArrayList<Event>();
    }

    public void add(Event e){
        events.add(e);
    }

    public void runAll() {
        if(events.size() > 0) {
            queueSize = events.size();
            currentEvent = 0;
            Event e = getNext();
            while(e != null) {
                e.run();
                e = getNext();
            }
            flush();
        }
    }

    public Event getNext() {
        if(currentEvent < queueSize) {
            currentEvent++;
            return events.get(currentEvent-1);
        }
        return null;
    }

    public void flush() {
        events.clear();
    }

}