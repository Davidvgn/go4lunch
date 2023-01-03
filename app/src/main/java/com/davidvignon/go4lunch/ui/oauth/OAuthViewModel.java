package com.davidvignon.go4lunch.ui.oauth;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.davidvignon.go4lunch.data.FirestoreRepository;
import com.davidvignon.go4lunch.data.users.User;
import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;


@HiltViewModel
public class OAuthViewModel extends ViewModel {

    @NonNull
    private final FirestoreRepository firebaseRepository;


    @Inject
    public OAuthViewModel(@NonNull FirestoreRepository firebaseRepository) {
        this.firebaseRepository = firebaseRepository;

    }

    public LiveData<User> getFacebookUserInfo(AccessToken token){
        return firebaseRepository.getUserSignedInWithFacebook(token);
    }

    public LiveData<User> getGoogleUserInfo(Task<GoogleSignInAccount> completedTask){
        return firebaseRepository.getUserSignedInWithGoogle(completedTask);
    }
}
