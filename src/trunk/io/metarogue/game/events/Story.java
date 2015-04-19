package io.metarogue.game.events;

import io.metarogue.game.events.time.Moment;
import io.metarogue.game.events.time.Timestamp;
import io.metarogue.game.events.actions.Action;
import io.metarogue.util.Timer;

public class Story extends StoryComposite {

    // Turns passed before "live" turn
    int totalTurns;
    // Number of turns passed before creation. For example if client connects at the display of turn 80 then this is 79
    int startingTurn;

    boolean playing;

    float animationStartingProgress = 0; // Progress that current animation started playing at
    long animationStartTime; // Nanosecond that currently displayed action(s)/event(s) started playing

    Timestamp displayStamp; // T:S:E:A timestamp of moment being displayed
    Moment displayMoment; // Moment being displayed

    boolean tracking = false; // Larger tracking operations should be multithreaded, this boolean is for locking

    TurnCollection turns;

    public Story(int turnsFromBefore) {
        turns= new TurnCollection(1000, turnsFromBefore); //TODO: Make history size configurable
        this.startingTurn = turnsFromBefore;
    }

    public void update() {
        if(tracking) {
            // See if tracking is done in other thread, when multi-threading implemented
            return;
        }
        if(playing) {

        }
    }

    /**
     * Tracks like a video/movie, moving the game state forward or backwards by finding and running
     * all actions between the currently displayed action and the destination specified.
     * @param destinationTimestamp Int array representing Turn/SubTurn/Event/Action/Progress to "track" to
     */
    public void track(Timestamp destinationTimestamp) {
        // Don't attempt if we're already tracking in another thread.
        if(!tracking) {
            // Don't bother if the timestamp is the same as ours
            if(!displayStamp.isSame(destinationTimestamp)) {
                // Don't bother if destination turn doesn't exist
                Moment destinationMoment = getMoment(destinationTimestamp);
                if(destinationMoment != null) {
                    displayStamp.setProgress(destinationTimestamp.getProgress());
                    if(turns.containsTurn(destinationTimestamp.getTurn())) {
                        Timestamp timeDelta = displayStamp.getDelta(destinationTimestamp);
                        int changeAmount = timeDelta.getAmount();
                        // Set progress, as it doesn't need to change game state only animations
                        animationStartTime = Timer.getNanoTime();
                        // Run or reverse all turns, subturns, events, and actions to that point
                        // TODO: do this on another thread for a frame if the difference is large enough?
                        if(timeDelta.isPositive()) {
                            if(changeAmount > 1) {
                                displayMoment.getEvent().runFrom(displayStamp.getAction());
                                if(changeAmount > 2) {
                                    displayMoment.getSubTurn().runFrom(displayStamp.getEvent());
                                    if(changeAmount > 3) {
                                        run(displayStamp.getTurn()+1, destinationTimestamp.getTurn()-1);
                                    }
                                    destinationMoment.getTurn().runTo(destinationTimestamp.getSubTurn());
                                }
                                destinationMoment.getSubTurn().runTo(destinationTimestamp.getEvent());
                            }
                            destinationMoment.getEvent().runThrough(destinationTimestamp.getAction());
                        }
                        if(!timeDelta.isPositive()) {
                            if(changeAmount > 1) {
                                displayMoment.getEvent().reverseFrom(displayStamp.getAction());
                                if(changeAmount > 2) {
                                    displayMoment.getSubTurn().reverseBefore(displayStamp.getEvent());
                                    if(changeAmount > 3) {
                                        reverse(displayStamp.getTurn()-1, destinationTimestamp.getTurn()+1);
                                    }
                                    destinationMoment.getTurn().reverseTo(destinationTimestamp.getSubTurn());
                                }
                                destinationMoment.getSubTurn().reverseTo(destinationTimestamp.getEvent());
                            }
                            destinationMoment.getEvent().reverseTo(destinationTimestamp.getAction());
                        }
                        displayMoment = destinationMoment;
                        displayStamp = destinationTimestamp;
                    }
                }
            }
        }
    }

    public void addEvent(Event e) {
        turns.newTurn().getSubTurn(0).addEvent(e);
    }

    /**
     * Get Moment containing a full, non-null Turn, SubTurn, Event, and Action
     * @param ts Timestamp of what the moment should have
     * @return Proper moment or null if any part is invalid
     */
    public Moment getMoment(Timestamp ts) {
        Turn t;
        SubTurn s;
        Event e;
        Action a;
        t = getTurn(ts.getTurn());
        if(t != null) {
            s = t.getSubTurn(ts.getSubTurn());
            if(s != null) {
                e = s.getEvent(ts.getEvent());
                if(e != null) {
                    a = e.getAction(ts.getAction());
                    if(a != null) {
                        return new Moment(t, s, e, a, ts);
                    }
                }
            }
        }
        return null;
    }

    public Action getAction(Timestamp t) {
        Moment m = getMoment(t);
        if(m != null) {
            return m.getAction();
        }
        return null;
    }

    public Moment getNextMoment() {
        // See if there is another moment and return it.
        // Basically keep checking if there is a non-empty action, then event, then subturn, etc....
        Timestamp t = displayStamp.copy();
        t.changeAction(1);
        Moment m = getMoment(t);
        if(m != null) return m;
        t.setAction(0);
        t.changeEvent(1);
        m = getMoment(t);
        if(m != null) return m;
        t.setEvent(0);
        t.changeSubTurn(1);
        m = getMoment(t);
        if(m != null) return m;
        t.setSubTurn(0);
        t.changeTurn(1);
        m = getMoment(t);
        if(m != null) return m;
        return null;
    }

    // If another moment exists, run and return true. If not, return false.
    public boolean runNextMoment() {
        Moment m = getNextMoment();
        if(m != null) {
            m.getAction().run();
            displayMoment = m;
            displayStamp = m.getTimestamp();
            return true;
        }
        return false;
    }

    public Moment getPreviousMoment() {
        // See if there is a previous moment and return it.
        // Basically keep checking if there is a non-empty action, then event, then subturn, etc....
        Timestamp t = displayStamp.copy();
        t.changeAction(-1);
        Moment m = getMoment(t);
        if(m != null) return m;
        t.setAction(0);
        t.changeEvent(-1);
        m = getMoment(t);
        if(m != null) return m;
        t.setEvent(0);
        t.changeSubTurn(-1);
        m = getMoment(t);
        if(m != null) return m;
        t.setSubTurn(0);
        t.changeTurn(-1);
        m = getMoment(t);
        if(m != null) return m;
        return null;
    }

    // If a previous moment exists, run and return true. If not, return false.
    public boolean runPreviousMoment() {
        Moment m = getPreviousMoment();
        if(m != null) {
            m.getAction().run();
            displayMoment = m;
            displayStamp = m.getTimestamp();
            return true;
        }
        return false;
    }

    public Action getFirstAction() {
        if(getSize() > 0) {
            return getTurn(0).getFirstAction();
        }
        return null;
    }

    public void play() {
        playing = true;
    }

    public void pause() {
        playing = false;
    }

    public void togglePlay() {
        if(playing) {
            playing = false;
        } else {
            playing = true;
        }
    }

    public Turn getTurn(int i) {
        return turns.getTurn(i);
    }

    public int getSize() {
        return turns.getSize();
    }

    public StoryComponent getStoryComponent(int i) {
        return turns.getTurn(i);
    }

}