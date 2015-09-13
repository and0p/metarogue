package io.metarogue.game.listener;

import io.metarogue.util.messagesystem.Listener;
import io.metarogue.util.messagesystem.message.Message;
import io.metarogue.util.messagesystem.message.game.player.PlayerQuit;
import io.metarogue.util.messagesystem.message.game.player.PlayerSkeleton;

public class GameListener implements Listener {

    public void receive(Message m) {
        if(m instanceof PlayerQuit) {
            m.run();
        }
        if(m instanceof PlayerSkeleton) {
            m.run();
        }
    }
}