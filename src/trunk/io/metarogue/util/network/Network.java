package io.metarogue.util.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import io.metarogue.util.network.message.NetworkMessageImpl;
import io.metarogue.util.network.message.TextMessage;
import io.metarogue.util.network.message.connection.ConnectionMessage;
import io.metarogue.util.network.message.game.BlockChange;

public class Network {

    public Network() {
        // Auto-generated constructor
    }

    // This registers objects that are going to be sent over the network.
    static public void register (EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(TextMessage.class);
        kryo.register(String[].class);
        // Register concrete types
        kryo.register(ConnectionMessage.class);
        kryo.register(BlockChange.class);
    }

}