package com.davidvignon.go4lunch.data;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;
import java.util.Objects;

public class Chat {

    @NonNull
    private final String workmateName;
    @NonNull
    private final String workmatePicture;

    public Chat (String workmateName, String workmatePicture) {
        this.workmateName = workmateName;
        this.workmatePicture = workmatePicture;

    }

    @NonNull
    public String getWorkmateName() {
        return workmateName;
    }

    @NonNull
    public String getWorkmatePicture() {
        return workmatePicture;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chat chat = (Chat) o;
        return workmateName.equals(chat.workmateName) && workmatePicture.equals(chat.workmatePicture);
    }

    @Override
    public int hashCode() {
        return Objects.hash(workmateName, workmatePicture);
    }

    @Override
    public String toString() {
        return "Chat{" +
            "workmateName='" + workmateName + '\'' +
            ", workmatePicture='" + workmatePicture + '\'' +
            '}';
    }
}

