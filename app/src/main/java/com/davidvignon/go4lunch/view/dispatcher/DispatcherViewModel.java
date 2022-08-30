package com.davidvignon.go4lunch.view.dispatcher;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.davidvignon.go4lunch.view.utils.SingleLiveEvent;
import com.google.firebase.auth.FirebaseAuth;

public class DispatcherViewModel extends ViewModel {

    @NonNull
    private final SingleLiveEvent<DispatcherViewAction> viewActionSingleLiveEvent = new SingleLiveEvent<>();

    public DispatcherViewModel(@NonNull FirebaseAuth firebaseAuth) {
        // User not connected
        if (firebaseAuth.getCurrentUser() == null) {
            viewActionSingleLiveEvent.setValue(DispatcherViewAction.GO_TO_CONNECT_SCREEN);
        } else {
            viewActionSingleLiveEvent.setValue(DispatcherViewAction.GO_TO_MAIN_SCREEN);
        }
    }

    @NonNull
    public SingleLiveEvent<DispatcherViewAction> getViewActionSingleLiveEvent() {
        return viewActionSingleLiveEvent;
    }
}
