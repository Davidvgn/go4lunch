package com.davidvignon.go4lunch.ui.chat;

import androidx.annotation.NonNull;

import java.util.Objects;

public class ChatMessageViewState {

    @NonNull
    private final String id;
    @NonNull
    private final String workmateName;

    @NonNull
    private final String message;
    @NonNull
    private final String time;

    public ChatMessageViewState(String id, String workmateName, String message, String time) {
        this.id = id;
        this.workmateName = workmateName;
        this.message = message;
        this.time = time;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getWorkmateName() {
        return workmateName;
    }

    @NonNull
    public String getMessage() {
        return message;
    }

    @NonNull
    public String getTime() {
        return time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatMessageViewState that = (ChatMessageViewState) o;
        return id.equals(that.id) && workmateName.equals(that.workmateName) && message.equals(that.message) && time.equals(that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, workmateName, message, time);
    }

    @Override
    public String toString() {
        return "ChatMessageViewState{" +
            "id='" + id + '\'' +
            ", workmateName='" + workmateName + '\'' +
            ", message='" + message + '\'' +
            ", time='" + time + '\'' +
            '}';
    }
}
