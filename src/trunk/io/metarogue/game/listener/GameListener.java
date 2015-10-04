package io.metarogue.game.listener;

import io.metarogue.Main;
import io.metarogue.game.gamemessage.GameMessage;
import io.metarogue.util.messagesystem.Listener;
import io.metarogue.util.messagesystem.message.Message;
import io.metarogue.game.gamemessage.player.PlayerQuit;
import io.metarogue.game.gamemessage.player.PlayerSkeleton;

public class GameListener implements Listener {

    public void receive(Message m) {
        if(m instanceof GameMessage) {
            GameMessage gm = (GameMessage)m;
            // See if game is playing forward or backwards
            if(Main.getGame().getStory().isPlayingForward()) {
                gm.run();
            } else {
                gm.reverse();
            }
        }
    }

}