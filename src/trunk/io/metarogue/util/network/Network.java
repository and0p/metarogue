package io.metarogue.util.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import io.metarogue.game.timeline.animation.Animation;
import io.metarogue.util.messagesystem.message.chat.ChatMessage;
import io.metarogue.util.messagesystem.message.connection.RegistrationMessage;
import io.metarogue.util.messagesystem.message.connection.PingMessage;
import io.metarogue.game.gamemessage.world.BlockChange;
import io.metarogue.game.gamemessage.player.PlayerAssignment;
import io.metarogue.game.gamemessage.player.PlayerQuit;
import io.metarogue.game.gamemessage.player.PlayerSkeleton;
import io.metarogue.util.messagesystem.message.skeleton.GameSkeleton;

public class Network {

    public Network() {
        // Auto-generated constructor
    }

    // This registers objects that are going to be sent over the network.
    static public void register (EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        // Register Java classes
        kryo.register(java.util.ArrayList.class);
        // Register concrete Message classes
        kryo.register(RegistrationMessage.class);
        kryo.register(PingMessage.class);
        kryo.register(BlockChange.class);
        kryo.register(PlayerAssignment.class);
        kryo.register(PlayerQuit.class);
        kryo.register(PlayerSkeleton.class);
        kryo.register(GameSkeleton.class);
        kryo.register(ChatMessage.class);
        // Register game classes
        kryo.register(Animation.class);
    }

}