package io.metarogue.server.user;

import com.esotericsoftware.kryonet.Connection;
import io.metarogue.util.messagesystem.message.Message;

import java.util.ArrayList;

public class UserConnection extends Connection {

    User user;
    ArrayList<Message> messageQueue;

    boolean ready = false;

    // Number of possibly exploitive messages?
    int badMessages = 0;

    public UserConnection() {
        messageQueue = new ArrayList<Message>();
    }

    public void addMessage(Message m) {
        messageQueue.add(m);
    }

    public ArrayList<Message> getMessageQueue() {
        return messageQueue;
    }

    public void clearMessages() {
        messageQueue.clear();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

}