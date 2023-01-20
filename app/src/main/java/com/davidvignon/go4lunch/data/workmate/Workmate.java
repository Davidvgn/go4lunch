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
    @NonNull
    private  String selectedRestaurantName;

    public Workmate() {
    }

    public Workmate(@NonNull String id, @NonNull String name, @NonNull String email, @NonNull String picturePath, @Nullable String selectedRestaurant,  @NonNull String selectedRestaurantName) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.picturePath = picturePath;
        this.selectedRestaurant = selectedRestaurant;
        this.selectedRestaurantName = selectedRestaurantName;
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

    @NonNull
    public String getSelectedRestaurantName() {
        return selectedRestaurantName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Workmate workmate = (Workmate) o;
        return id.equals(workmate.id) && name.equals(workmate.name) && email.equals(workmate.email) && picturePath.equals(workmate.picturePath) && Objects.equals(selectedRestaurant, workmate.selectedRestaurant) && selectedRestaurantName.equals(workmate.selectedRestaurantName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, picturePath, selectedRestaurant, selectedRestaurantName);
    }

    @Override
    public String toString() {
        return "Workmate{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", email='" + email + '\'' +
            ", picturePath='" + picturePath + '\'' +
            ", selectedRestaurant='" + selectedRestaurant + '\'' +
            ", selectedRestaurantName='" + selectedRestaurantName + '\'' +
            '}';
    }
}
