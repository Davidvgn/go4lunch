package com.davidvignon.go4lunch.data.users;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Objects;

public class User {

    @NonNull
    private String id;
    @NonNull
    private String name;
    @Nullable
    private String picturePath;
    @NonNull
    private String email;

    @Nullable
    private List<String> favoritesRestaurants;

    @Nullable
    private String selectedRestaurant;

    public User(){
    }

    public User(@NonNull String id, @NonNull String name, @Nullable String picturePath, @NonNull String email, @Nullable List<String> favoritesRestaurants, @Nullable String selectedRestaurant ) {
        this.id = id;
        this.name = name;
        this.picturePath = picturePath;
        this.email = email;
        this.favoritesRestaurants = favoritesRestaurants;
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

    @Nullable
    public String getPicturePath() {
        return picturePath;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    @Nullable
    public List<String> getFavoritesRestaurants() {
        return favoritesRestaurants;
    }

    @Nullable
    public String getSelectedRestaurant() {
        return selectedRestaurant;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id) && name.equals(user.name) && Objects.equals(picturePath, user.picturePath) && email.equals(user.email) && Objects.equals(favoritesRestaurants, user.favoritesRestaurants) && Objects.equals(selectedRestaurant, user.selectedRestaurant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, picturePath, email, favoritesRestaurants, selectedRestaurant);
    }

    @Override
    public String toString() {
        return "User{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", picturePath='" + picturePath + '\'' +
            ", email='" + email + '\'' +
            ", favoritesRestaurants=" + favoritesRestaurants +
            ", selectedRestaurant='" + selectedRestaurant + '\'' +
            '}';
    }
}
