package com.davidvignon.go4lunch.ui.workmates;

import androidx.annotation.NonNull;

import java.util.Objects;

public class WorkmatesViewStates {

    @NonNull
    private final String name;

    @NonNull
    private final String picturePath;

    public WorkmatesViewStates(@NonNull String name, @NonNull String picturePath) {
        this.name = name;
        this.picturePath = picturePath;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getPicturePath() {
        return picturePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkmatesViewStates that = (WorkmatesViewStates) o;
        return name.equals(that.name) && picturePath.equals(that.picturePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, picturePath);
    }

    @Override
    public String toString() {
        return "WorkmatesViewStates{" +
            ", name='" + name + '\'' +
            ", picturePath='" + picturePath + '\'' +
            '}';
    }
}



