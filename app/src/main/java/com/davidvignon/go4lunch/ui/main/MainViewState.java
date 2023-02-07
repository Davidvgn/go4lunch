package com.davidvignon.go4lunch.ui.main;

import androidx.annotation.NonNull;

import java.util.Objects;

public class MainViewState {
    private final String name;
    private final String mail;
    private final String imageUrl;

    public MainViewState(String name, String mail, String imageUrl) {
        this.name = name;
        this.mail = mail;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getMail() {
        return mail;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MainViewState that = (MainViewState) o;
        return Objects.equals(name, that.name) && Objects.equals(mail, that.mail) && Objects.equals(imageUrl, that.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, mail, imageUrl);
    }

    @NonNull
    @Override
    public String toString() {
        return "MainViewState{" +
            "name='" + name + '\'' +
            ", mail='" + mail + '\'' +
            ", imageUrl='" + imageUrl + '\'' +
            '}';
    }
}
