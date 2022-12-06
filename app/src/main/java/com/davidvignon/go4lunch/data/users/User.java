package com.davidvignon.go4lunch.data.users;

import androidx.annotation.NonNull;

import java.util.Objects;

public class User {

    @NonNull
    private String id;
    @NonNull
    private String name;
    @NonNull
    private String picturePath;
    @NonNull
    private String email;
    @NonNull
    private String auth;

    public User(){
    }

    public User(String id, String name, String picturePath, String email, String auth) {
        this.id = id;
        this.name = name;
        this.picturePath = picturePath;
        this.email = email;
        this.auth = auth;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public String getEmail() {
        return email;
    }

    public String getAuth() {
        return auth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id) && name.equals(user.name) && picturePath.equals(user.picturePath) && email.equals(user.email) && auth.equals(user.auth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, picturePath, email, auth);
    }

    @Override
    public String toString() {
        return "User{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", picturePath='" + picturePath + '\'' +
            ", email='" + email + '\'' +
            ", auth='" + auth + '\'' +
            '}';
    }
}
