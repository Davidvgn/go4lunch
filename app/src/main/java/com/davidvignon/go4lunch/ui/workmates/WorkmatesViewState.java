package com.davidvignon.go4lunch.ui.workmates;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class WorkmatesViewState {

    @NonNull
    private final String id;

    @NonNull
    private final String name;

    @NonNull
    private final String picturePath;

    @Nullable
    private final String selectedRestaurant;
    @Nullable
    private final String selectedRestaurantName;

    public WorkmatesViewState(@NonNull String id, @NonNull String name, @NonNull String picturePath, @Nullable String selectedRestaurant, @Nullable String selectedRestaurantName) {
        this.id =id;
        this.name = name;
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
    public String getPicturePath() {
        return picturePath;
    }


    @Nullable
    public String getSelectedRestaurant() {
        return selectedRestaurant;
    }

    @Nullable
    public String getSelectedRestaurantName() {
        return selectedRestaurantName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkmatesViewState that = (WorkmatesViewState) o;
        return id.equals(that.id) && name.equals(that.name) && picturePath.equals(that.picturePath) && Objects.equals(selectedRestaurant, that.selectedRestaurant) && Objects.equals(selectedRestaurantName, that.selectedRestaurantName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, picturePath, selectedRestaurant, selectedRestaurantName);
    }

    @NonNull
    @Override
    public String toString() {
        return "WorkmatesViewState{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", picturePath='" + picturePath + '\'' +
            ", selectedRestaurant='" + selectedRestaurant + '\'' +
            ", selectedRestaurantName='" + selectedRestaurantName + '\'' +
            '}';
    }
}



