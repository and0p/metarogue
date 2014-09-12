package io.metarogue.util.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import io.metarogue.util.network.message.TextMessage;

public class Network {

    public Network() {
        // Auto-generated constructor
    }

    // This registers objects that are going to be sent over the network.
    static public void register (EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(TextMessage.class);
        kryo.register(String[].class);
    }

}