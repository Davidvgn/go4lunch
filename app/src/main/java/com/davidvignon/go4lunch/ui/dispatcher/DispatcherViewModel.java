package com.davidvignon.go4lunch.ui.dispatcher;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.davidvignon.go4lunch.ui.utils.SingleLiveEvent;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class DispatcherViewModel extends ViewModel {

    @Inject
    public DispatcherViewModel(@NonNull FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getCurrentUser() == null) {
            viewActionSingleLiveEvent.setValue(DispatcherViewAction.GO_TO_CONNECT_SCREEN);
        } else {
            viewActionSingleLiveEvent.setValue(DispatcherViewAction.GO_TO_MAIN_SCREEN);
        }
    }

    @NonNull
    private final SingleLiveEvent<DispatcherViewAction> viewActionSingleLiveEvent = new SingleLiveEvent<>();

    @NonNull
    public SingleLiveEvent<DispatcherViewAction> getViewActionSingleLiveEvent() {
        return viewActionSingleLiveEvent;
    }
}
