package io.metarogue.util.messagesystem.message.gamestate;

import io.metarogue.Main;
import io.metarogue.game.gamemessage.GameMessage;

public class PlayDirectionChange extends GameMessage {

    // TODO: Make this an enum instead of this boolean, whatever, nonsense
    boolean direction;

    public PlayDirectionChange(boolean direction) {
        this.direction = direction;
    }

    public void run() {
        Main.getGame().getStory().setPlayingForward(direction);
    }

    public void reverse() {
        run();
    }

}