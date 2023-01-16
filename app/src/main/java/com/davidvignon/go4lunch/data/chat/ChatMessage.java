package com.davidvignon.go4lunch.data.chat;

import java.util.Objects;

public class ChatMessage {

    String sender;
    String receiver;
    String message;
    String time;


    public ChatMessage() {
    }

    public ChatMessage(String sender, String receiver, String message, String time) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.time = time;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        return time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatMessage that = (ChatMessage) o;
        return Objects.equals(sender, that.sender) && Objects.equals(receiver, that.receiver) && Objects.equals(message, that.message) && Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sender, receiver, message, time);
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
            "sender='" + sender + '\'' +
            ", receiver='" + receiver + '\'' +
            ", message='" + message + '\'' +
            ", time='" + time + '\'' +
            '}';
    }
}

