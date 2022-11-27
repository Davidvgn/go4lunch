package com.davidvignon.go4lunch.ui.workmates;

import androidx.annotation.NonNull;

import java.util.Objects;

public class WorkmatesViewStates {

    @NonNull
    private final String name;

    @NonNull
    private final String picturePath;

    @NonNull
    private final String email;

    public WorkmatesViewStates(int workmatesId, @NonNull String name, @NonNull String picturePath, @NonNull String email) {
        this.name = name;
        this.picturePath = picturePath;
        this.email = email;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getPicturePath() {
        return picturePath;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkmatesViewStates that = (WorkmatesViewStates) o;
        return name.equals(that.name) && picturePath.equals(that.picturePath) && email.equals(that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, picturePath, email);
    }

    @Override
    public String toString() {
        return "WorkmatesViewStates{" +
            ", name='" + name + '\'' +
            ", picturePath='" + picturePath + '\'' +
            ", email='" + email + '\'' +
            '}';
    }
}



