package com.davidvignon.go4lunch.ui.workmates;

import androidx.annotation.NonNull;

import java.util.Objects;

public class WorkmatesViewState {

    @NonNull
    private final String id;

    @NonNull
    private final String name;

    @NonNull
    private final String picturePath;

    public WorkmatesViewState(@NonNull String id, @NonNull String name, @NonNull String picturePath) {
        this.id =id;
        this.name = name;
        this.picturePath = picturePath;
    }

    @NonNull
    public String getId() {
        return id;
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
        WorkmatesViewState that = (WorkmatesViewState) o;
        return id.equals(that.id) && name.equals(that.name) && picturePath.equals(that.picturePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, picturePath);
    }

    @Override
    public String toString() {
        return "WorkmatesViewStates{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", picturePath='" + picturePath + '\'' +
            '}';
    }
}



