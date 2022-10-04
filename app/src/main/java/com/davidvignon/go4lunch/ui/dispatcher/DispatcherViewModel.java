package com.davidvignon.go4lunch.ui.dispatcher;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.davidvignon.go4lunch.ui.utils.SingleLiveEvent;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ViewModelComponent;
import dagger.hilt.android.scopes.ViewModelScoped;

@Module
@InstallIn(ViewModelComponent.class)
public class DispatcherViewModel extends ViewModel {

    private FirebaseAuth firebaseAuth;

    @Provides
    @ViewModelScoped
    public FirebaseAuth provideFirebaseAuth(){
        firebaseAuth = FirebaseAuth.getInstance();
        return firebaseAuth;
    }

    @Inject
    public DispatcherViewModel() {
        provideFirebaseAuth();
        if (firebaseAuth.getCurrentUser() == null) {
            viewActionSingleLiveEvent.setValue(DispatcherViewAction.GO_TO_CONNECT_SCREEN);
        } else {
            viewActionSingleLiveEvent.setValue(DispatcherViewAction.GO_TO_MAIN_SCREEN);
        }
    }

    @NonNull
    private final SingleLiveEvent<DispatcherViewAction> viewActionSingleLiveEvent = new SingleLiveEvent<>();

//    @Inject
//    public DispatcherViewModel(@NonNull FirebaseAuth firebaseAuth) {
////
////        provideFirebaseAuth();
//        // User not connected
//        if (firebaseAuth.getCurrentUser() == null) {
//            viewActionSingleLiveEvent.setValue(DispatcherViewAction.GO_TO_CONNECT_SCREEN);
//        } else {
//            viewActionSingleLiveEvent.setValue(DispatcherViewAction.GO_TO_MAIN_SCREEN);
//        }
//    }

    @NonNull
    public SingleLiveEvent<DispatcherViewAction> getViewActionSingleLiveEvent() {
        return viewActionSingleLiveEvent;
    }
}
