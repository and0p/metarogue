package io.metarogue.util.messagesystem.message.game.player;

import io.metarogue.Main;
import io.metarogue.util.messagesystem.message.MessageImpl;

public class PlayerAssignment extends MessageImpl {

    int playerID;

    public PlayerAssignment() {
    }

    public PlayerAssignment(int playerID) {
        this.playerID = playerID;
    }

    public boolean isTCP() {
        return true;
    }

    public void runAsClient() {
        Main.getClient().setPlayer(playerID);
    }

}