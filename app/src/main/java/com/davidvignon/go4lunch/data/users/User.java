package com.davidvignon.go4lunch.data.users;


import java.util.Objects;

public class User {

    private String name;
    private String picturePath;
    private String email;
    private String auth;

    public User(){
    }

    public User(String name, String picturePath, String email, String auth) {
        this.name = name;
        this.picturePath = picturePath;
        this.email = email;
        this.auth = auth;
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
        return Objects.equals(name, user.name) && Objects.equals(picturePath, user.picturePath) && Objects.equals(email, user.email) && Objects.equals(auth, user.auth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, picturePath, email, auth);
    }

    @Override
    public String toString() {
        return "User{" +
            "name='" + name + '\'' +
            ", picturePath='" + picturePath + '\'' +
            ", email='" + email + '\'' +
            ", auth='" + auth + '\'' +
            '}';
    }
}
