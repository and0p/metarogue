package io.metarogue.util.message;

import java.util.ArrayList;

public class MessagePump {

    ArrayList<Message> queue;

    public MessagePump() {
        queue = new ArrayList<Message>();
    }

}