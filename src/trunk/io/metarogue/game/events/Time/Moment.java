package io.metarogue.game.events.Time;

import io.metarogue.game.events.Event;
import io.metarogue.game.events.Story;
import io.metarogue.game.events.SubTurn;
import io.metarogue.game.events.Turn;
import io.metarogue.game.events.actions.Action;

/**
 * Collection of turn/subturn/event/action
 * In other words concrete objects for all nested objects at a specific "moment" in time.
 * Methods to collect this from a "timestamp" (array representing t:s:e:a)
 */

public class Moment {

    Timestamp timestamp;

    Turn turn;
    SubTurn subTurn;
    Event event;
    Action action;

    public Moment(Turn turn, SubTurn subTurn, Event event, Action action) {
        this.turn = turn;
        this.subTurn = subTurn;
        this.event = event;
        this.action = action;
    }

    public Moment(Story story, Timestamp t) {
        timestamp = t;
        turn = story.getTurn(t.getTurn());
        subTurn =  turn.getSubTurn(t.getSubturn());
        event = subTurn.getEvent(t.getEvent());
        if (event != null) {
            action = event.getAction(t.getAction());
        } else {
            action = null;
        }
    }

    public Turn getTurn() {
        return turn;
    }

    public void setTurn(Turn turn) {
        this.turn = turn;
    }

    public SubTurn getSubTurn() {
        return subTurn;
    }

    public void setSubTurn(SubTurn subTurn) {
        this.subTurn = subTurn;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

}
