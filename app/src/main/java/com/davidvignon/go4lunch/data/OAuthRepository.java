package com.davidvignon.go4lunch.data;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.davidvignon.go4lunch.data.users.User;
import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class OAuthRepository {

    public static final String COLLECTION_PATH_USERS = "users";
    @NonNull
    private final FirebaseFirestore firebaseFirestore;

    @Inject
    public OAuthRepository(@NonNull FirebaseFirestore firebaseFirestore) {
        this.firebaseFirestore = firebaseFirestore;
    }

    public LiveData<User> getUserSignedInWithFacebook(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();

        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.i("Authentication", "getUserSignedInWithFacebook.onComplete: isSuccessful");

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (user != null
                    && user.getPhotoUrl() != null) {
                    String id = user.getUid();
                    String fullName = user.getDisplayName();
                    String[] email = new String[1];
                    for (UserInfo userInfo : user.getProviderData()) {
                        if (userInfo.getEmail() != null) {
                            email[0] = userInfo.getEmail();
                            break;
                        }
                    }
                    String picturePath = user.getPhotoUrl().toString() + "?access_token=" + token.getToken();

                    String uuid = user.getUid();

                    CollectionReference usersRef = firebaseFirestore.collection("users");

                    Query query = usersRef.whereEqualTo("id", uuid);

                    query.get().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            QuerySnapshot querySnapshot = task1.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                firebaseFirestore
                                    .collection(COLLECTION_PATH_USERS)
                                    .document(user.getUid())
                                    .get()
                                    .addOnCompleteListener(task2 -> {
                                        if (task2.isSuccessful()) {
                                            userMutableLiveData.setValue(task2.getResult().toObject(User.class));
                                        } else {
                                            Log.e("UserRepository", "get failed with ", task.getException());
                                        }
                                    });
                            } else {
                                if (fullName != null && email[0] != null) {
                                    DocumentReference documentReference = firebaseFirestore.collection(COLLECTION_PATH_USERS).document(id);
                                    User currentUser = new User(
                                        id,
                                        fullName,
                                        picturePath,
                                        email[0],
                                        null,
                                        null
                                    );
                                    userMutableLiveData.setValue(currentUser);

                                    documentReference.set(currentUser).addOnSuccessListener(unused -> Log.i("Authentication", "onSuccessFirestore: "));
                                } else {
                                    Log.e("OAuthRepository", "getUserSignedInWithFacebook() FAILED ! No email available.");
                                }
                            }
                        }
                    });
                }
            } else {
                Log.e("OAuthRepository", "Task Failed !");
            }
        });

        return userMutableLiveData;

    }

    public LiveData<User> getUserSignedInWithGoogle(Task<GoogleSignInAccount> completedTask) {
        MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
        try {
            GoogleSignInAccount googleAccount = completedTask.getResult(ApiException.class);
            AuthCredential credential = GoogleAuthProvider.getCredential(googleAccount.getIdToken(), null);
            FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.i("Authentication", "onComplete: isSuccessful");

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if (user != null) {
                        String id = user.getUid();
                        String fullName = user.getDisplayName();
                        String[] email = new String[1];
                        for (UserInfo userInfo : user.getProviderData()) {
                            if (userInfo.getEmail() != null) {
                                email[0] = userInfo.getEmail();
                                break;
                            }
                        }

                        Uri pictureUri = user.getPhotoUrl();
                        String picturePath = pictureUri != null ? pictureUri.toString() : null;


                        String uuid = user.getUid();

                        CollectionReference usersRef = firebaseFirestore.collection("users");

                        Query query = usersRef.whereEqualTo("id", uuid);

                        query.get().addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                QuerySnapshot querySnapshot = task1.getResult();
                                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                    firebaseFirestore
                                        .collection(COLLECTION_PATH_USERS)
                                        .document(user.getUid())
                                        .get()
                                        .addOnCompleteListener(task2 -> {
                                            if (task2.isSuccessful()) {
                                                userMutableLiveData.setValue(task2.getResult().toObject(User.class));
                                            } else {
                                                Log.e("UserRepository", "get failed with ", task.getException());
                                            }
                                        });
                                } else {

                                    if (fullName != null && email[0] != null) {
                                        DocumentReference documentReference = firebaseFirestore.collection(COLLECTION_PATH_USERS).document(id);

                                        User currentUser = new User(
                                            id,
                                            fullName,
                                            picturePath,
                                            email[0],
                                            null,
                                            null
                                        );
                                        userMutableLiveData.setValue(currentUser);

                                        documentReference.set(currentUser).addOnSuccessListener(unused -> Log.i("Authentication", "onSuccessFirestore: "));
                                    } else {
                                        Log.e("OAuthRepository", "getUserSignedInWithGoogle() FAILED ! No email available.");
                                    }
                                }
                            }
                        });
                    }
                } else {
                    Log.e("OAuthRepository", "Task Failed !");
                }
            });
        } catch (ApiException e) {
            Log.w("Authentication", "signInResult:failed code=" + e.getStatusCode());
        }
        return userMutableLiveData;
    }
}
