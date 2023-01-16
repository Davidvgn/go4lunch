package com.davidvignon.go4lunch.data.workmate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class Workmate {

    @NonNull
    private  String id;

    @NonNull
    private  String name;

    @NonNull
    private  String email;

    @NonNull
    private  String picturePath;

    @Nullable
    private  String selectedRestaurant;

    public Workmate() {
    }

    public Workmate(@NonNull String id, @NonNull String name, @NonNull String email, @NonNull String picturePath, @Nullable String selectedRestaurant) {
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
        Workmate workmate = (Workmate) o;
        return id.equals(workmate.id) && name.equals(workmate.name) && email.equals(workmate.email) && picturePath.equals(workmate.picturePath) && Objects.equals(selectedRestaurant, workmate.selectedRestaurant);
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
            ", selectedRestaurant='" + selectedRestaurant + '\'' +
            '}';
    }
}
