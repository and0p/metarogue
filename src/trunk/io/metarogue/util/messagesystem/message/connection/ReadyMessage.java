package io.metarogue.util.messagesystem.message.connection;

import io.metarogue.Main;
import io.metarogue.server.user.User;
import io.metarogue.util.messagesystem.message.MessageImpl;

public class ReadyMessage extends MessageImpl {

    public boolean verify() {
        //check if name exists, if it does increment and verify() again (which will increment again and so on...)
        return true;
    }

    public void runAsServer() {
        // TODO: Shouldn't be accessing what should be package-private...
        User u = Main.getServer().getUser(getSender());

    }

    public boolean isTCP() {
        return true;
    }

}