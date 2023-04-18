package com.davidvignon.go4lunch.data.chat_message;

import androidx.annotation.NonNull;

import java.util.Objects;

public class ChatMessage {

    @NonNull
    private String uuid;
    @NonNull
    private String senderId;
    @NonNull
    private String receiverId;
    @NonNull
    private String message;
    private long epochMilli;

    public ChatMessage() {
    }

    public ChatMessage(@NonNull String uuid, @NonNull String senderId, @NonNull String receiverId, @NonNull String message,
        long epochMilli) {
        this.uuid = uuid;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.epochMilli = epochMilli;
    }

    @NonNull
    public String getUuid() {
        return uuid;
    }

    @NonNull
    public String getSenderId() {
        return senderId;
    }

    @NonNull
    public String getReceiverId() {
        return receiverId;
    }

    @NonNull
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


    @NonNull
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

