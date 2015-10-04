package io.metarogue.game.timeline;

import io.metarogue.Main;
import io.metarogue.game.timeline.animation.Animatable;
import io.metarogue.game.timeline.animation.Animation;
import io.metarogue.game.timeline.time.Moment;
import io.metarogue.game.timeline.time.Timestamp;
import io.metarogue.util.Timer;
import io.metarogue.game.gamemessage.GameMessage;

import java.util.ArrayList;

public class Story extends StoryComposite {

    // Number of turns passed before creation. For example if client connects at the display of turn 80 then this is 79
    Timestamp startingTimestamp;

    boolean playing;
    boolean playingForward = true;

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
            track(m);
        }
    }

    /**
     * Tracks like a video/movie, moving the game state forward or backwards by finding and running
     * all gameMessages between the currently displayed action and the destination specified.
     * @param destination Int array representing Turn/SubTurn/Event/GameMessage/Progress to "track" to
     */
    public void track(Moment destination) {
        // Don't attempt if we're already tracking in another thread.
        if(!tracking) {
            Timestamp displayStamp = displayMoment.getTimestamp();
            Timestamp destinationStamp = destination.getTimestamp();
            // Don't bother if the timestamp (sans animation progress) is the same as ours
            if(!displayStamp.isSame(destinationStamp)) {
                // Fast forward or rewind depending on if destination timestamp is in past or future
                if(displayStamp.isLessThan(destinationStamp)) {
                    boolean b = true; // Boolean for seeing if new moments exist
                    while((displayMoment.getTimestamp().isLessThan(destinationStamp) || displayStamp.isSame(destinationStamp)) && b) {
                        b = pushNextMoment();
                    }
                }
                else if(displayStamp.isGreaterThan(destinationStamp)) {
                    boolean b = true; // Boolean for seeing if new moments exist
                    while((displayMoment.getTimestamp().isGreaterThan(destinationStamp) || displayStamp.isSame(destinationStamp)) && b) {
                        b = pushLastMoment();
                    }
                }
                // Either way, set the animation progress to the one passed to this function
                displayMoment.getTimestamp().setProgress(destination.getTimestamp().getProgress());
            } else if(!displayStamp.hasSameProgress(destinationStamp)) {
                displayStamp.setProgress(destinationStamp.getProgress());
            }
            // Update the current animation(s)
            // TODO: displayMoment.getMessage().updateAnimation(displayMoment.getTimestamp().getProgress());
        }
    }

    // Tracking function if we only want to pass a timestamp
    public void track(Timestamp destinationStamp) {
        Moment m = getMoment(destinationStamp);
        if(m != null) track(m);
    }

    // Returns moment that should be running after x amount of milliseconds
    public Moment getMomentAfterMilliseconds(long timeLeft) {
        Moment m = displayMoment.copy();
        if(m != null) {
            // See if there is positive time remaining (or 0, as there could be a new instant animation)
            if(timeLeft >= 0) {
                // Add time of current animation progress to timeLeft and loop from there for simplicity
                float currentProgress = m.getTimestamp().getProgress();
                Animation a;
                float progress = 0;
                if(m instanceof Animatable) {
                    Animatable animatable = (Animatable)m;
                    a = animatable.getAnimation();
                    timeLeft += a.getMillisecondsFromProgress(currentProgress);
                    progress = a.getProgressAfterMilliseconds(timeLeft);
                }
                // See if animation is finished by this frame, and if so load the next.
                if(progress >= 1) {
                    Moment nextMoment = getNextMoment(m);
                    // If there is more timeleft at last moment's finish, loop through upcoming moments until out of time
                    while(progress >= 1 && nextMoment != null) {
                        // Get animation of current moment in loop
                        if(m instanceof Animatable) {
                            Animatable animatable = (Animatable)m;
                            a = animatable.getAnimation();
                            // Subtract it's duration from time delta
                            timeLeft -= a.getDuration();
                            // Set progress based on remainder of time delta into next animation
                            progress = a.getProgressAfterMilliseconds(timeLeft);
                        }
                        // Start pulling data on the next moment just in case we've completed this one too
                        m = nextMoment.copy();
                        nextMoment = getNextMoment(m);
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
        GameMessage m;
        t = getTurn(ts.getTurn());
        if(t != null) {
            s = t.getSubTurn(ts.getSubTurn());
            if(s != null) {
                e = s.getEvent(ts.getEvent());
                if(e != null) {
                    m = e.getMessage(ts.getMessage());
                    if(m != null) {
                        return new Moment(t, s, e, m, ts);
                    }
                }
            }
        }
        return null;
    }

    public Moment getNextMoment() {
        return getNextMoment(displayMoment);
    }

    public Moment getNextMoment(Moment referenceMoment) {
        // See if there is another moment and return it.
        // Basically keep checking if there is a non-empty action, then event, then subturn, etc....
        Moment m = referenceMoment;
        Timestamp t = m.getTimestamp().copy();
        int turn = t.getTurn();
        int subturn = t.getSubTurn();
        int event = t.getEvent();
        int action = t.getMessage();
        // Check at each level, return if more moments exist.
        if(m.getEvent().hasNewer(action)) {
            t.incrementAction();
            return getMoment(t);
        } else if (m.getSubTurn().hasNewer(event)) {
            t.incrementEvent();
            return getMoment(t);
        } else if (m.getTurn().hasNewer(subturn)) {
            t.incrementSubTurn();
            return getMoment(t);
        } else if (this.hasNewer(turn)) {
            t.incrementTurn();
            return getMoment(t);
        } else {
            return null;
        }
    }

    // If another moment exists, run and return true. If not, return false.
    public boolean pushNextMoment() {
        Moment m = getNextMoment();
        if(m != null) {
            Main.getGame().addGameMessage(m.getMessage());
            displayMoment = m;
            displayStamp = m.getTimestamp();
            return true;
        }
        return false;
    }

    // See if there is a previous moment and return it.
    public Moment getPreviousMoment(Moment referenceMoment) {
        Moment m = referenceMoment.copy();
        Timestamp ts = m.getTimestamp().copy();
        // See if there are previous gameMessages in event
        if(ts.getMessage() > 0) {
            ts.changeAction(-1);
            return getMoment(ts);
        } else if(ts.getEvent() > 0) {
            // Get previous event
            ts.changeEvent(-1);
            Event e = m.getSubTurn().getEvent(ts.getEvent());
            // Set action part of timestamp to last action in this event
            ts.setMessage(e.getSize() - 1);
            return getMoment(ts);
        } else if(ts.getSubTurn() > 0) {
            // Get previous subturn
            ts.changeSubTurn(-1);
            SubTurn s = m.getTurn().getSubTurn(ts.getSubTurn());
            // Set event to be last from that subturn
            ts.setEvent(s.getSize() - 1);
            Event e = s.getEvent(ts.getEvent());
            // Set action part of timestamp to last action in this event
            ts.setMessage(e.getSize() - 1);
            return getMoment(ts);
        } else if (ts.getTurn() > turns.getFirst()) {
            // Get previous turn
            ts.changeTurn(-1);
            Turn t = turns.getTurn(ts.getTurn());
            // Get previous subturn
            ts.setSubTurn(t.getSize()-1);
            SubTurn s = t.getSubTurn(ts.getSubTurn());
            // Set event to be last from that subturn
            ts.setEvent(s.getSize() - 1);
            Event e = s.getEvent(ts.getEvent());
            // Set action part of timestamp to last action in this event
            ts.setMessage(e.getSize() - 1);
            return getMoment(ts);
        }
        return null;
    }

    public boolean pushLastMoment() {
        Moment m = getPreviousMoment(displayMoment);
        if(m != null) {
            Main.getGame().addGameMessage(m.getMessage());
            displayMoment = m;
            displayStamp = m.getTimestamp();
            return true;
            }
        return false;
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

    public void setPlayingForward(boolean b) {
        playingForward = b;
    }

    public boolean isPlayingForward() {
        return playingForward;
    }

}