package io.metarogue.game.scope;

public class ScopeConfiguration {

    // This number is the amount of chunks outward PAST the center chunk that a GameObject sits on to load
    int scopeSize;

    public ScopeConfiguration(int scopeSize) {
        this.scopeSize = scopeSize;
    }

    public int getScopeSize() {
        return scopeSize;
    }

    public int getDiameter() {
        return scopeSize*2+1;
    }

    public void setScopeSize(int scopeSize) {
        this.scopeSize = scopeSize;
    }

}
