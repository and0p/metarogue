package io.metarogue.game.events;

import io.metarogue.game.events.animation.Animation;
import io.metarogue.game.events.time.Moment;
import io.metarogue.game.events.time.Timestamp;
import io.metarogue.game.events.actions.Action;
import io.metarogue.util.Timer;

import java.util.ArrayList;

public class Story extends StoryComposite {

    // Number of turns passed before creation. For example if client connects at the display of turn 80 then this is 79
    Timestamp startingTimestamp;

    boolean playing;

    Timestamp displayStamp; // T:S:E:A:P timestamp of moment being displayed
    Moment displayMoment; // Moment being displayed

    Timestamp liveStamp;
    Moment liveMoment;

    boolean tracking = false; // Larger tracking operations should be multi-threaded, this boolean is for locking

    boolean subTurnFinished = false;
    int numberOfSides;

    TurnCollection turns;

    public Story(int numberOfSides) {
        turns = new TurnCollection(1000, 0); //TODO: Make history size configurable
        turns.init();
        this.numberOfSides = numberOfSides;
        startingTimestamp = new Timestamp(0,0,0,0);
        displayStamp = startingTimestamp.copy();
        liveStamp = new Timestamp(0,0,0,0);
        displayMoment = getMoment(displayStamp);
        liveMoment = getMoment(liveStamp);
        playing = true;
    }

    // Create new turn, acknowledge this new turn is the "live" (most up-to-date) one
    public void newTurn() {
        liveMoment.setTurn(turns.newTurn());
        liveMoment.setSubTurn(liveMoment.getTurn().newSubTurn());
        liveMoment.getTimestamp().incrementTurn();
    }

    // Create new subturn, acknowledge this new subturn is the "live" (most up-to-date) one
    public void newSubTurn() {
        // See if we're all out of subturns for this turn, if so make a new turn
        if(liveMoment.getTimestamp().getSubTurn() >= numberOfSides-1) {
            newTurn();
        } else {
            liveMoment.setSubTurn(liveMoment.getTurn().newSubTurn());
            liveMoment.getTimestamp().incrementSubTurn();
        }
        subTurnFinished = false;
    }

    // Add event, if external source has informed us that the last subturn has ended create new subturn/turn
    public void addEvent(Event e) {
        addSubTurnIfNeeded();
        liveMoment.getSubTurn().addEvent(e);
        liveMoment.getTimestamp().incrementEvent();
    }

    public void addEventAndEndSubturn(Event e) {
        addSubTurnIfNeeded();
        liveMoment.getSubTurn().addEvent(e);
        liveStamp.incrementEvent();
        subTurnFinished = true;
    }

    public void addEvents(ArrayList<Event> events) {
        newSubTurn();
        for(Event e : events) {
            addEvent(e);
        }
        subTurnFinished = true;
    }

    public void setSubTurnFinished() {
        subTurnFinished = true;
    }

    public void addSubTurnIfNeeded() {
        if(subTurnFinished) {
            newSubTurn();
        }
    }

    public void update() {
        if(tracking) {
            // See if tracking is done in other thread, when multi-threading implemented
            return;
        }
        if(playing) {
            // Get the delta time for this frame
            long timeLeft = Timer.convertNanosecondsToMilliseconds(Timer.getDelta());
            Moment m = getMomentAfterMilliseconds(timeLeft);
            track(m.getTimestamp());
        }
    }

    /**
     * Tracks like a video/movie, moving the game state forward or backwards by finding and running
     * all actions between the currently displayed action and the destination specified.
     * @param destinationStamp Int array representing Turn/SubTurn/Event/Action/Progress to "track" to
     */
    public void track(Timestamp destinationStamp) {
        // Don't attempt if we're already tracking in another thread.
        if(!tracking) {
            // Don't bother if the timestamp (sans animation progress) is the same as ours
            if(!displayStamp.isSame(destinationStamp)) {
                Moment destinationMoment = getMoment(destinationStamp);
                // Don't bother if destination turn doesn't exist
                if(destinationMoment != null) {
                    // Fast forward or rewind depending on if destination timestamp is in past or future
                    if(displayStamp.isLessThan(destinationStamp)) {
                        boolean b = true; // Boolean for seeing if new moments exist
                        while((displayStamp.isLessThan(destinationStamp) || displayStamp.isSame(destinationStamp)) && b) {
                            b = runNextMoment();
                        }
                    }
                    else if(displayStamp.isGreaterThan(destinationStamp)) {
                        boolean b = true; // Boolean for seeing if new moments exist
                        while((displayStamp.isGreaterThan(destinationStamp) || displayStamp.isSame(destinationStamp)) && b) {
                            b = reverseCurrentMoment();
                        }
                    }
                    // Either way, set the animation progress to the one passed to this function
                    displayStamp.setProgress(destinationStamp.getProgress());
                }
            } else if(!displayStamp.hasSameProgress(destinationStamp)) {
                displayStamp.setProgress(destinationStamp.getProgress());
            }
        }
    }

    public Moment getMomentAfterMilliseconds(long timeLeft) {
        Moment m = displayMoment.copy();
        if(m != null) {
            // See if there is positive time remaining (or 0, there could be a new instant animation)
            if(timeLeft >= 0) {
                // Add time of current animation progress to timeLeft and loop from there for simplicity
                float currentProgress = m.getTimestamp().getProgress();
                Animation a = m.getAction().getAnimation();
                timeLeft += a.getMillisecondsFromProgress(currentProgress);
                float progress = a.getProgressAfterMilliseconds(timeLeft);
                if(progress >= 1) {
                    Moment nm = getNextMoment(m);
                    while(timeLeft >= 0 && nm != null) {
                        m = nm;
                        a = m.getAction().getAnimation();
                        progress = a.getProgressAfterMilliseconds(timeLeft);
                        if(progress > 1) {
                            timeLeft -= a.getDuration();
                            nm = getNextMoment(m);
                        }
                    }
                }
                m.getTimestamp().setProgress(progress);
                return m;
            } else {
                return m;
            }
        }
       return m;
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

    public Moment getNextMoment() {
        // See if there is another moment and return it.
        // Basically keep checking if there is a non-empty action, then event, then subturn, etc....
        Moment m = displayMoment.copy();
        if(m.getEvent())
        return null;
    }

    public Moment getNextMoment(Moment referenceMoment) {
        // See if there is another moment and return it.
        // Basically keep checking if there is a non-empty action, then event, then subturn, etc....
        Timestamp t = referenceMoment.getTimestamp();
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

    public boolean reverseCurrentMoment() {
        Moment m = getPreviousMoment();
        if(m != null) {
            displayMoment.getAction().reverse();
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

    public Timestamp getDisplayStamp() {
        return displayStamp;
    }

    public StoryComponent getStoryComponent(int i) {
        return turns.getTurn(i);
    }

}