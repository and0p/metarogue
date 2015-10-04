package io.metarogue.util.messagesystem.message;

import io.metarogue.util.messagesystem.Listener;
import io.metarogue.util.messagesystem.message.Message;

import java.util.ArrayList;

public class MessagePump {

    ArrayList<Listener> listeners;

    ArrayList<Message> messages;
    ArrayList<Message> unsanitizedMessages;

    public MessagePump() {
        listeners = new ArrayList<Listener>();
        messages = new ArrayList<Message>();
        unsanitizedMessages = new ArrayList<Message>();
    }

    public synchronized void dispatch() {
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

    public synchronized void addMessage(Message m) {
        messages.add(m);
    }

    public synchronized void addUnsanitizedMessage(Message m) {
        unsanitizedMessages.add(m);
    }

    public synchronized void sanitizeMessages() {
        for(Message m : unsanitizedMessages) {
            if(m.sanitize()) {
                messages.add(m);
            } else {
                // TODO: Act on possibly exploitive message ie add to user count and possibly kick?
            }
        }
        unsanitizedMessages.clear();
    }

}