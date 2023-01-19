package com.davidvignon.go4lunch.data.chatMessage;

import java.util.Objects;

public class ChatMessage {

    private String uuid;
    private String senderId;
    private String receiverId;
    private String message;
    private long epochMilli;

    public ChatMessage() {
    }

    public ChatMessage(String uuid, String senderId, String receiverId, String message, long epochMilli) {
        this.uuid = uuid;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.epochMilli = epochMilli;
    }

    public String getUuid() {
        return uuid;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getMessage() {
        return message;
    }

    public long getEpochMilli() {
        return epochMilli;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatMessage that = (ChatMessage) o;
        return epochMilli == that.epochMilli && Objects.equals(uuid, that.uuid) && Objects.equals(senderId, that.senderId) && Objects.equals(receiverId, that.receiverId) && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, senderId, receiverId, message, epochMilli);
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
            "uuid='" + uuid + '\'' +
            ", senderId='" + senderId + '\'' +
            ", receiverId='" + receiverId + '\'' +
            ", message='" + message + '\'' +
            ", epochMilli=" + epochMilli +
            '}';
    }
}

