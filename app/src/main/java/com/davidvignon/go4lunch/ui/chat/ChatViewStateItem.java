package com.davidvignon.go4lunch.ui.chat;

import androidx.annotation.NonNull;

import java.util.Objects;

public class ChatViewStateItem {

    @NonNull
    private final String id;
    @NonNull
    private final String workmateName;
    @NonNull
    private final String message;
    @NonNull
    private final String time;

    private final boolean isFromCurrentUser;

    public ChatViewStateItem(@NonNull String id, @NonNull String workmateName, @NonNull String message, @NonNull String time, boolean isFromCurrentUser) {
        this.id = id;
        this.workmateName = workmateName;
        this.message = message;
        this.time = time;
        this.isFromCurrentUser = isFromCurrentUser;
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

    public boolean isFromCurrentUser() {
        return isFromCurrentUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatViewStateItem that = (ChatViewStateItem) o;
        return isFromCurrentUser == that.isFromCurrentUser && id.equals(that.id) && workmateName.equals(that.workmateName) && message.equals(that.message) && time.equals(that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, workmateName, message, time, isFromCurrentUser);
    }

    @Override
    public String toString() {
        return "ChatViewStateItem{" +
            "id='" + id + '\'' +
            ", workmateName='" + workmateName + '\'' +
            ", message='" + message + '\'' +
            ", time='" + time + '\'' +
            ", isFromCurrentUser=" + isFromCurrentUser +
            '}';
    }
}
