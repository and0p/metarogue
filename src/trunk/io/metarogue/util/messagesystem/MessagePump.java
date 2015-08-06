package io.metarogue.util.messagesystem;

import io.metarogue.util.messagesystem.message.Message;

import java.util.ArrayList;

public class MessagePump {

    ArrayList<Listener> listeners;

    ArrayList<Message> messages;

    public MessagePump() {
        listeners = new ArrayList<Listener>();
        messages = new ArrayList<Message>();
    }

    public void dispatch() {
        for(Message m : messages) {
            for(Listener l : listeners) {
                l.receive(m);
            }
        }
        messages.clear();
    }

    public void register(Listener l) {
        listeners.add(l);
    }

    public void addMessage(Message m) {
        messages.add(m);
    }

}