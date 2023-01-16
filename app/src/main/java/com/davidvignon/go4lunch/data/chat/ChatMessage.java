package com.davidvignon.go4lunch.data.chat;

import java.time.LocalDateTime;
import java.util.Objects;

public class ChatMessage {
    //todo david checker naming xml et dans tous les codes

    String recipient;
    String message;
    String time;


    public ChatMessage() {
    }


    public ChatMessage(String recipient, String message, String time) {
        this.recipient = recipient;
        this.message = message;
        this.time = time;
    }

    public String getRecipient() {
        return recipient;
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
        return Objects.equals(recipient, that.recipient) && Objects.equals(message, that.message) && Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipient, message, time);
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
            "recipient='" + recipient + '\'' +
            ", message='" + message + '\'' +
            ", time='" + time + '\'' +
            '}';
    }
}

