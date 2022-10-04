package com.davidvignon.go4lunch.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.davidvignon.go4lunch.ui.dispatcher.DispatcherViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class ViewModelFactory implements ViewModelProvider.Factory {

//    private static volatile ViewModelFactory factory;
//
//    private final FirebaseAuth firebaseAuth;
//
//    public static ViewModelFactory getInstance() {
//        if (factory == null) {
//            synchronized (ViewModelFactory.class) {
//                if (factory == null) {
//                    factory = new ViewModelFactory();
//                }
//            }
//        }
//        return factory;
//    }
//
//    public ViewModelFactory() {
//        firebaseAuth = FirebaseAuth.getInstance();
//    }
//
//    @SuppressWarnings("unchecked")
//    @NonNull
//    @Override
//    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
//        if (modelClass.isAssignableFrom(DispatcherViewModel.class)){
//            return (T) new DispatcherViewModel(firebaseAuth);
//        }
//        throw new IllegalArgumentException("Unknown ViewModel class");
//    }
}
