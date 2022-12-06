package com.davidvignon.go4lunch.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class Workmates {

    @NonNull
    private final String id;

    @NonNull
    private final String name;

    @NonNull
    private final String email;

    @NonNull
    private final String picturePath;

    @Nullable
    private final String selectedRestaurant;

    public Workmates(@NonNull String id, @NonNull String name, @NonNull String email, String picturePath, @Nullable String selectedRestaurant) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.picturePath = picturePath;
        this.selectedRestaurant = selectedRestaurant;
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
    public String getEmail() {
        return email;
    }

    @NonNull
    public String getPicturePath() {
        return picturePath;
    }

    @Nullable
    public String getSelectedRestaurant() {
        return selectedRestaurant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Workmates workmates = (Workmates) o;
        return id.equals(workmates.id) && name.equals(workmates.name) && email.equals(workmates.email) && picturePath.equals(workmates.picturePath) && Objects.equals(selectedRestaurant, workmates.selectedRestaurant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, picturePath, selectedRestaurant);
    }

    @Override
    public String toString() {
        return "Workmates{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", email='" + email + '\'' +
            ", picturePath='" + picturePath + '\'' +
            ", choice='" + selectedRestaurant + '\'' +
            '}';
    }
}
