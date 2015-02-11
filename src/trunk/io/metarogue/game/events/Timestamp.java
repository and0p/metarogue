package io.metarogue.game.events;

import io.metarogue.game.events.actions.Action;

public class Timestamp {

    Turn turn;
    SubTurn subTurn;
    Event event;
    Action action;

    public Timestamp(Turn turn, SubTurn subTurn, Event event, Action action) {
        this.turn = turn;
        this.subTurn = subTurn;
        this.event = event;
        this.action = action;
    }

    public Timestamp(Story story, TimestampInt t) {
        turn = story.getTurn(t.getTurn());
        subTurn =  turn.getSubTurn(t.getSubturn());
        event = subTurn.getEvent(t.getEvent());
        action = event.getAction(t.getAction());
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
