package net.and0.metarogue.controller.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

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