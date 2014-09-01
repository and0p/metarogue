package io.metarogue.game.events;

import io.metarogue.game.events.actions.Action;

import java.util.ArrayList;

public class Event {

    ArrayList<Action> actions;

    public Event() {
        ArrayList<Action> actions = new ArrayList<Action>();
    }

    public Event(ArrayList<Action> actions) {
        this.actions = actions;
    }

}
