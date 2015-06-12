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

    float animationProgress = 0;
    float animationStartingProgress = 0; // Progress that current animation started playing at
    long animationStartTime = 0; // Nanosecond that currently displayed action(s)/event(s) started playing

    Timestamp displayStamp; // T:S:E:A:P timestamp of moment being displayed
    Moment displayMoment; // Moment being displayed

    Timestamp liveStamp;
    Moment liveMoment;

    boolean tracking = false; // Larger tracking operations should be multithreaded, this boolean is for locking

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
    }

    // Create new subturn, acknowledge this new subturn is the "live" (most up-to-date) one
    public void newSubTurn() {
        liveMoment.setSubTurn(liveMoment.getTurn().newSubTurn());
    }

    // Add event, if external source has informed us that the last subturn has ended create new subturn/turn
    public void addEvent(Event e) {
        if(subTurnFinished) {
            if(liveStamp.getSubTurn() == numberOfSides - 1) {
                newTurn();
                liveStamp.incrementTurn();
                newSubTurn();
                liveMoment.getSubTurn().addEvent(e);
                liveMoment.setEvent(e);
                subTurnFinished = false;
            } else {
                newSubTurn();
                liveMoment.getSubTurn().addEvent(e);
                liveMoment.setEvent(e);
                liveStamp.incrementSubTurn();
                subTurnFinished = false;
            }
        } else {
            displayMoment.getSubTurn().addEvent(e);
            liveStamp.incrementEvent();
        }
    }

    public void addEventAndEndSubturn(Event e) {
        displayMoment.getSubTurn().addEvent(e);
        liveStamp.incrementEvent();
        subTurnFinished = true;
    }

    public void addEvents(ArrayList<Event> events) {
        for(Event e : events) {
            addEvent(e);
        }
        subTurnFinished = true;
    }

    public void setSubTurnFinished() {
        subTurnFinished = true;
    }

    public void update() {
        if(tracking) {
            // See if tracking is done in other thread, when multi-threading implemented
            return;
        }
        if(playing) {
            // Get the delta time for this frame
            long timeLeft = Timer.convertNanosecondsToMilliseconds(Timer.getDelta());
            // See if there's anything new to display
            Moment m = getNextMoment();
            boolean moreMoments = false;
            if(m != null) {
                moreMoments = true;
            }
            // While there is still progress to display....
            while((timeLeft > 0 && moreMoments) || (timeLeft > 0 && displayMoment.getAction().getAnimation() != null && animationProgress < 1)) {
                // Get current animation duration and subtract time left, unless there is no animation for this action
                if(displayMoment.getAction().getAnimation() != null) {
                    if(animationProgress < 1) {
                        int animDuration = displayMoment.getAction().getAnimation().getDuration();
                        int millisecondsRemaining = animDuration - (int)(animDuration * animationProgress);
                        int millisecondsProgress = animDuration - millisecondsRemaining + (int)timeLeft;
                        timeLeft -= millisecondsRemaining;
                        animationProgress = displayMoment.getAction().getAnimation().getProgressAfterMilliseconds(millisecondsProgress);
                    }
                    if(animationProgress >= 1) {
                        displayMoment.getAction().finishAnimation();
                        if(moreMoments) {
                            moreMoments = runNextMoment();
                            animationProgress = 0;
                        }
                    }
                    if(timeLeft < 0) {
                        displayMoment.getAction().updateAnimation(animationProgress);
                    }
                } else {
                    moreMoments = runNextMoment();
                }
            }
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
                    // ... and reset the animation starting time and progress so we know where to play from here
                    animationStartingProgress = displayStamp.getProgress();
                    animationStartTime = Timer.getFrameTime();
                }
            } else if(!displayStamp.hasSameProgress(destinationStamp)) {
                displayStamp.setProgress(destinationStamp.getProgress());
                animationStartingProgress = displayStamp.getProgress();
                animationStartTime = Timer.getFrameTime();
            }
        }
    }

    public Moment getMomentAfterMilliseconds(long timeLeft) {
        Moment m = displayMoment.copy();
        // See if there is positive time remaining (or 0, there could be a new instant animation)
        if(timeLeft >= 0) {
            // Add time of current animation progress to timeLeft and loop from there for simplicity
            float currentProgress = m.getTimestamp().getProgress();
            Animation a = m.getAction().getAnimation();
            timeLeft += a.getMillisecondsFromProgress(currentProgress);
            // TODO: How accurate is the above line? Chance that progress -> milliseconds is too inaccurate?
            // If there's still any time & moments left, keep looping through next moment until we're done
            Moment nm = getNextMoment();
            while(timeLeft >= 0 && nm != null) {
                // Subtract full duration of currently displaying animation from delta
                timeLeft -= m.getAction().getAnimation().getDuration();
                // If there is still time get next moment
                if(timeLeft >= 0) {
                    // Set m to previously retrieved next moment since it is now "live"
                    m = nm;
                    // ... and nm is the next moment after that, which needs to be non-null to keep the loop going
                    nm = getNextMoment(m);
                }
            }
            // Generate final moment to return by finding progress of "live" moment
            // If timeLeft is negative make positive
            if(timeLeft < 0) timeLeft *= -1;
            a = m.getAction().getAnimation();
            float p = a.getProgressAfterMilliseconds(timeLeft);
            m.getTimestamp().setProgress(p);
            return m;
        } else {
            return m;
        }
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

    // If a previous moment exists, run and return true. If not, return false.
    public boolean runPreviousMoment() {
        Moment m = getPreviousMoment();
        if(m != null) {
            m.getAction().reverse();
            displayMoment = m;
            displayStamp = m.getTimestamp();
            return true;
        }
        return false;
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