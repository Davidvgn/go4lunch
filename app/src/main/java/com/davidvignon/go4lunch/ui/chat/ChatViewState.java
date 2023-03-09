package com.davidvignon.go4lunch.ui.chat;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Objects;

public class ChatViewState {

    @NonNull
    private final String workmateName;
    @NonNull
    private final String workmatePicture;
    @NonNull
    private final List<ChatViewStateItem> messages;

    public ChatViewState(@NonNull String workmateName, @NonNull String workmatePicture, @NonNull List<ChatViewStateItem> messages) {
        this.workmateName = workmateName;
        this.workmatePicture = workmatePicture;
        this.messages = messages;
    }

    @NonNull
    public String getWorkmateName() {
        return workmateName;
    }

    @NonNull
    public String getWorkmatePicture() {
        return workmatePicture;
    }

    @NonNull
    public List<ChatViewStateItem> getMessages() {
        return messages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatViewState chatViewState = (ChatViewState) o;
        return workmateName.equals(chatViewState.workmateName) && workmatePicture.equals(chatViewState.workmatePicture) && messages.equals(chatViewState.messages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(workmateName, workmatePicture, messages);
    }

    @NonNull
    @Override
    public String toString() {
        return "Chat{" +
            "workmateName='" + workmateName + '\'' +
            ", workmatePicture='" + workmatePicture + '\'' +
            ", messages=" + messages +
            '}';
    }
}

