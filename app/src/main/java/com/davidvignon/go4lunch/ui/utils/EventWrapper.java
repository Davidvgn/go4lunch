package com.davidvignon.go4lunch.ui.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;

public class EventWrapper<T> {

    private final T content;

    private boolean hasBeenHandled;

    public EventWrapper(@NonNull T content) {
        this.content = content;
    }

    @Nullable
    public T getContentIfNotHandled() {
        if (hasBeenHandled) {
            return null;
        } else {
            hasBeenHandled = true;
            return content;
        }
    }

    @VisibleForTesting
    public T peekContent() {
        return content;
    }
}
