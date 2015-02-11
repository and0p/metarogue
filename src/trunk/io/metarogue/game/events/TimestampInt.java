package io.metarogue.game.events;

// Wrapped series of integers that represent a point in the game's time.

public class TimestampInt {

    int turn;
    int subturn;
    int event;
    int action;
    float progress;

    public TimestampInt(int turn, int subturn, int event, int action, float progress) {
        this.turn = turn;
        this.subturn = subturn;
        this.event = event;
        this.action = action;
        this.progress = progress;
    }

    public TimestampInt(int turn, int subturn, int event, int action) {
        this.turn = turn;
        this.subturn = subturn;
        this.event = event;
        this.action = action;
        progress = 0;
    }

    public TimestampInt(int turn, int subturn, int event) {
        this.turn = turn;
        this.subturn = subturn;
        this.event = event;
        action = 0;
        progress = 0;
    }

    public TimestampInt(int turn, int subturn) {
        this.turn = turn;
        this.subturn = subturn;
        this.event = 0;
        this.action = 0;
        progress = 0;
    }

    public TimestampInt(int turn) {
        this.turn = turn;
        this.subturn = 0;
        this.event = 0;
        this.action = 0;
        progress = 0;
    }

    // Compare other timestamp, return true if all fields are identical
    public boolean compare(TimestampInt t) {
        if(this.turn == t.getTurn() && this.subturn == t.getSubturn() && this.event == t.getEvent() && this.action == t.getAction() && this.progress == t.getProgress()) return true;
        return false;
    }

    // Return difference between two timestamps
    public TimestampInt getDelta(TimestampInt t) {
        return new TimestampInt(turn - t.getTurn(), subturn - t.getSubturn(), event - t.getEvent(), action - t.getAction(), progress - t.getProgress());
    }

    // Return how precise 0-4
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

    // Jesus I won't even try to explain this one yet
    public int getAmount() {
        if(progress != 0) {
            return 0;
        }
        if(action != 0) {
            return 1;
        }
        if(event != 0) {
            return 2;
        }
        if(subturn != 0) {
            return 3;
        }
        return 4;
    }

    public boolean isPositive() {
        if(turn >= 0 && subturn >= 0 && event >= 0 && action >= 0 && progress >= 0) {
            return true;
        }
        return false;
    }

    public TimestampInt copy() {
        return new TimestampInt(turn, subturn, event, action, progress);
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public int getSubturn() {
        return subturn;
    }

    public void setSubturn(int subturn) {
        this.subturn = subturn;
    }

    public int getEvent() {
        return event;
    }

    public void setEvent(int event) {
        this.event = event;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public float getProgress() {return progress; }

    public void setProgress(float progress) { this.progress = progress; }

}
