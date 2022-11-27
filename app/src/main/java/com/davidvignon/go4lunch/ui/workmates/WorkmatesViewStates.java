package com.davidvignon.go4lunch.ui.workmates;

import androidx.annotation.NonNull;

import java.util.Objects;

public class WorkmatesViewStates {

    @NonNull
   private final String email;

    @NonNull
   private final String name;

    public WorkmatesViewStates(@NonNull String email, @NonNull String name) {
        this.email = email;
        this.name = name;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkmatesViewStates that = (WorkmatesViewStates) o;
        return email.equals(that.email) && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, name);
    }

    @Override
    public String toString() {
        return "WorkmatesViewStates{" +
            "email='" + email + '\'' +
            ", name='" + name + '\'' +
            '}';
    }
}
