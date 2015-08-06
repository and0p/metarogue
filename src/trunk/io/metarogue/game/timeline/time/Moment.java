package io.metarogue.game.timeline.time;

import io.metarogue.game.timeline.Event;
import io.metarogue.game.timeline.SubTurn;
import io.metarogue.game.timeline.Turn;
import io.metarogue.game.timeline.actions.Action;

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

    public Moment() {
    }

    public Moment(Turn turn, SubTurn subTurn, Event event, Action action) {
        this.turn = turn;
        this.subTurn = subTurn;
        this.event = event;
        this.action = action;
        timestamp = null;
    }

    public Moment(Turn turn, SubTurn subTurn, Event event, Action action, Timestamp timestamp) {
        this.turn = turn;
        this.subTurn = subTurn;
        this.event = event;
        this.action = action;
        this.timestamp = timestamp;
    }

    public Moment copy() {
        return new Moment(turn, subTurn, event, action, timestamp);
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

    public Timestamp getTimestamp() { return timestamp; }

    public void setTimestamp(Timestamp timestamp) { this.timestamp = timestamp; }

}