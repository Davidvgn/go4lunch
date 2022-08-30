package com.davidvignon.go4lunch.view;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.davidvignon.go4lunch.view.dispatcher.DispatcherViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private static ViewModelFactory factory;

    private FirebaseAuth firebaseAuth;


    public static ViewModelFactory getInstance() {
        if (factory == null) {
            synchronized (ViewModelFactory.class) {
                if (factory == null) {
                    factory = new ViewModelFactory();
                }
            }
        }
        return factory;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(DispatcherViewModel.class)){
            return (T) new DispatcherViewModel(firebaseAuth);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
