package io.metarogue.game.events.time;

// Wrapped series of integers that represent a point in the game's time.

public class Timestamp {

    int turn;
    int subturn;
    int event;
    int action;
    float progress;

    public Timestamp(int turn, int subturn, int event, int action, float progress) {
        this.turn = turn;
        this.subturn = subturn;
        this.event = event;
        this.action = action;
        this.progress = progress;
    }

    public Timestamp(int turn, int subturn, int event, int action) {
        this.turn = turn;
        this.subturn = subturn;
        this.event = event;
        this.action = action;
        progress = 0;
    }

    public Timestamp(int turn, int subturn, int event) {
        this.turn = turn;
        this.subturn = subturn;
        this.event = event;
        action = 0;
        progress = 0;
    }

    public Timestamp(int turn, int subturn) {
        this.turn = turn;
        this.subturn = subturn;
        this.event = 0;
        this.action = 0;
        progress = 0;
    }

    public Timestamp(int turn) {
        this.turn = turn;
        this.subturn = 0;
        this.event = 0;
        this.action = 0;
        progress = 0;
    }

    /**
     * See if passed TimestampInt is identical to this one
     * @param t TimestampInt to isSame
     * @return True if they are identical
     */
    public boolean isSame(Timestamp t) {
        if(this.turn == t.getTurn() && this.subturn == t.getSubTurn() && this.event == t.getEvent() && this.action == t.getAction()) return true;
        return false;
    }

    public boolean hasSameProgress(Timestamp t) {
        if(this.progress == t.getProgress()) return true;
        return false;
    }

    public boolean isGreaterThan(Timestamp t) {
        if(getTurn() > t.getTurn()) return true;
        if(getTurn() == t.getTurn()) {
            if(getSubTurn() > t.getSubTurn()) return true;
            if(getSubTurn() == t.getSubTurn()) {
                if(getEvent() > t.getEvent()) return true;
                if(getEvent() == t.getEvent()) {
                    if(getAction() > t.getAction()) return true;
                }
            }
        }
        return false;
    }

    public boolean isLessThan(Timestamp t) {
        if(getTurn() < t.getTurn()) return true;
        if(getTurn() == t.getTurn()) {
            if(getSubTurn() < t.getSubTurn()) return true;
            if(getSubTurn() == t.getSubTurn()) {
                if(getEvent() < t.getEvent()) return true;
                if(getEvent() == t.getEvent()) {
                    if(getAction() < t.getAction()) return true;
                }
            }
        }
        return false;
    }

    /** Return difference between current timestamp and passed timestamp
     * NOTICE: Because there is no set number of any composite, this isn't like a decimal system i.e. if you went
     * from 1.0.0.5.0 to 10.0.0.1.0 then the "action" change would be -4 despite the whole thing being forward in time.
     * Basically this is only useful for seeing the amount / precision of change in other methods.
     * @param t Timestamp to get delta of
     * @return New TimestampInt as result
     */
    public Timestamp getDelta(Timestamp t) {
        return new Timestamp(t.getTurn()-turn, t.getSubTurn()-subturn, t.getEvent()-event, t.getAction()-action, t.getProgress()-progress);
    }

    /**
     * Gives int representing how precise a timestamp (probably a delta) is
     * @return 0-4 for if precision is at turn, subturn, event, action, or animationProgress level
     */
    public int getPrecision() {
        if(progress != 0) {
            return 4;
        }
        if(action != 0) {
            return 3;
        }
        if(event != 0) {
            return 2;
        }
        if(subturn != 0) {
            return 1;
        }
        return 0;
    }

    /**
     * Get the amount of change represented by a timestamp (which should represent a "delta" or change in time)
     * @return 0-4 depending on change being only animationProgress or a full action/event/subturn/ and finally turn.
     */
    public int getAmount() {
        if(turn != 0) {
            return 4;
        }
        if(subturn != 0) {
            return 3;
        }
        if(event != 0) {
            return 2;
        }
        if(action != 0) {
            return 1;
        }
        return 0;
    }

    /**
     * Check if any change amount in time is ultimately forward or backwards in time
     * @return Returns true if time delta is positive or "forward" in time
     */
    public boolean isPositive() {
        if(turn > 0) {
            return true;
        }
        if(subturn > 0) {
            return true;
        }
        if(event > 0) {
            return true;
        }
        if(action > 0) {
            return true;
        }
        return false;
    }

    public Timestamp copy() {
        return new Timestamp(turn, subturn, event, action, progress);
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public void changeTurn(int i) {this.turn += i; }

    public int getSubTurn() {
        return subturn;
    }

    public void setSubTurn(int subturn) {
        this.subturn = subturn;
    }

    public void changeSubTurn(int i) {this.subturn += i; }

    public int getEvent() { return event; }

    public void setEvent(int event) {
        this.event = event;
    }

    public void changeEvent(int i) {this.event += i; }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public void changeAction(int i) {this.action += i; }

    public float getProgress() {return progress; }

    public void setProgress(float progress) { this.progress = progress; }

    public void incrementTurn() {
        turn = turn+1;
        subturn = 0;
        event = 0;
        action = 0;
        progress = 0;
    }

    public void incrementSubTurn() {
        subturn = subturn+1;
        event = 0;
        action = 0;
        progress = 0;
    }

    public void incrementEvent() {
        event = event+1;
        action = 0;
        progress = 0;
    }

    public void incrementAction() {
        action = action+1;
        progress = 0;
    }

}
