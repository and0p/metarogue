package io.metarogue.game.timeline;

public class StoryUpdate {

    // Actual StoryComposite we're wrapping
    StoryComposite storyComposite;
    // Time from the server, in-case that is ever relevant
    long serverTime;
    // Amount of GameMessages contained
    int messageCount;

    // Blank initializer for Kryonet
    public StoryUpdate() {}

    public StoryUpdate(StoryComposite storyComposite) {
        this.storyComposite = storyComposite;
    }

    public StoryComposite getStoryComposite() {
        return storyComposite;
    }

}
