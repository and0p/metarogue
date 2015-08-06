package io.metarogue.util.messagesystem.message.connection;

import io.metarogue.Main;
import io.metarogue.game.Player;
import io.metarogue.server.user.User;
import io.metarogue.util.messagesystem.message.MessageImpl;
import io.metarogue.util.messagesystem.message.game.player.PlayerAssignment;

public class RegistrationMessage extends MessageImpl {

    String name;
    String nick;
    String password;

    // Zero-arg constructor for Kryo
    // TODO: Write serializer myself? https://code.google.com/p/kryo/issues/detail?id=5
    public RegistrationMessage() {
    }

    public RegistrationMessage(String name, String password, String nick) {
        this.name = name;
        this.password = password; //TODO: Encrypt, etc
        this.nick = nick;
    }

    public RegistrationMessage(String nick) { this.nick = nick; }

    public boolean verify() {
        //check if name exists, if it does increment and verify() again (which will increment again and so on...)
        return true;
    }

    public void runAsServer() {
        // TODO: Shouldn't be accessing what should be package-private...
        User u = Main.getServer().getUser(getSender());
        u.register(nick);
        Player p = u.createPlayer();
        Main.getServer().sendMessage(u.getID(), Main.getGame().getSkeleton());
        Main.getServer().sendMessageToAll(p.getSkeleton());
        Main.getServer().sendMessage(u.getID(), new PlayerAssignment(u.getID()));
    }

    public boolean isTCP() {
        return true;
    }

}