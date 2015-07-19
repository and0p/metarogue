package io.metarogue.util.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import io.metarogue.game.events.animation.Animation;
import io.metarogue.util.network.message.connection.RegistrationMessage;
import io.metarogue.util.network.message.connection.PingMessage;
import io.metarogue.util.network.message.game.BlockChange;
import io.metarogue.util.network.message.skeleton.GameSkeleton;

public class Network {

    public Network() {
        // Auto-generated constructor
    }

    // This registers objects that are going to be sent over the network.
    static public void register (EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        // Register Java classes
        kryo.register(java.util.ArrayList.class);
        // Register concrete NetworkMessage classes
        kryo.register(RegistrationMessage.class);
        kryo.register(PingMessage.class);
        kryo.register(BlockChange.class);
        kryo.register(GameSkeleton.class);
        // Register game classes
        kryo.register(Animation.class);
    }

}