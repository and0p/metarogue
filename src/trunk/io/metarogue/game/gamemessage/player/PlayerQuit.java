package io.metarogue.game.gamemessage.player;

import io.metarogue.Main;
import io.metarogue.util.messagesystem.message.MessageImpl;

public class PlayerQuit extends MessageImpl {

    int playerID;

    public PlayerQuit() {

    }

    public PlayerQuit(int playerID) {
        this.playerID = playerID;
    }

    public boolean sanitize() {
        //TODO: Sanitize player object
        return true;
    }

    public void run() {
        Main.getGame().getPlayers().remove(playerID);
    }

    public boolean isTCP() {
        return true;
    }

}