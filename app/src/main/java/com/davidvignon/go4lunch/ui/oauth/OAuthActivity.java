package com.davidvignon.go4lunch.ui.oauth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.davidvignon.go4lunch.data.users.User;
import com.davidvignon.go4lunch.databinding.AuthActivityBinding;
import com.davidvignon.go4lunch.ui.main.MainActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class OAuthActivity extends AppCompatActivity {

    AuthActivityBinding binding;

    private static final int REQ_ONE_TAP = 2;
    private static final int RC_SIGN_IN = 3;
    private boolean showOneTapUI = true;

    private FirebaseAuth mAuth;
    CallbackManager callbackManager;

    private SignInClient oneTapClient;
    private GoogleSignInClient mGoogleSignInClient;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AuthActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseFirestore = FirebaseFirestore.getInstance();

        //GOOGLE
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        MaterialButton gooleSignInButton = binding.authGoogleLoginButton;

        gooleSignInButton.setOnClickListener(view -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });

        //FACEBOOK
        LoginButton facebookButton = binding.authFacebookLoginButton;

        callbackManager = CallbackManager.Factory.create();

        mAuth = FirebaseAuth.getInstance();

        oneTapClient = Identity.getSignInClient(this);

        facebookButton.setReadPermissions("email", "public_profile", "user_photos");

        facebookButton.registerCallback(callbackManager,
            new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    Log.d("DavidVgn", "onSuccess: " + loginResult);
                    handleFacebookAccessToken(loginResult.getAccessToken());
                    startActivity(new Intent(OAuthActivity.this, MainActivity.class));
                }

                @Override
                public void onCancel() {
                    Log.d("DavidVgn", "onCancel: ");
                }

                @Override
                public void onError(FacebookException exception) {
                    Log.d("DavidVgn", "onError: " + exception);
                }
            });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            //GOOGLE
            case RC_SIGN_IN:
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);
                break;
            //FACEBOOK
            case REQ_ONE_TAP:
                try {
                    SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
                    String idToken = credential.getGoogleIdToken();
                    String password = credential.getPassword();

                    if (idToken != null) {
                        // Got an ID token from Google. Use it to authenticate
                        // with your backend.
                        Log.d("DavidVgn", "Got ID token.");
                    } else if (password != null) {
                        // Got a saved username and password. Use them to authenticate
                        // with your backend.
                        Log.d("DavidVgn", "Got password.");
                    }
                } catch (ApiException e) {
                    switch (e.getStatusCode()) {
                        case CommonStatusCodes.CANCELED:
                            Log.d("DavidVgn", "One-tap dialog was closed.");
                            // Don't re-prompt the user.
                            showOneTapUI = false;
                            break;
                        case CommonStatusCodes.NETWORK_ERROR:
                            Log.d("DavidVgn", "One-tap encountered a network error.");
                            // Try again or just ignore.
                            break;
                        default:
                            Log.d("DavidVgn", "Couldn't get credential from result."
                                + e.getLocalizedMessage());
                            break;
                    }
                }
                break;
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        //FACEBOOK
        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        GraphRequest request = GraphRequest.newMeRequest(
            accessToken,
            (object, response) -> {
                // Application code
                try {
                    String fullName = object.getString("name");
                    String email = object.getString("email");
                    String picturePath = object.getJSONObject("picture").getJSONObject("data").getString("url");

                    DocumentReference documentReference = firebaseFirestore.collection("users").document(fullName);

                    User user = new User(
                        fullName,
                        picturePath,
                        email,
                        "Facebook"
                    );
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("DavidVgn", "onSuccessFirestore: ");
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name, email,link,picture.type(large)");
        request.setParameters(parameters);
        request.executeAsync();


        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    Log.d("DavidVgn", "onComplete: " + task);
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(OAuthActivity.this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show();
                }
            });
    }


    //GOOGLE
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(OAuthActivity.this);
        String personName = acct.getDisplayName();
        String personEmail = acct.getEmail();
        Uri picturePath = acct.getPhotoUrl();

        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            startActivity(new Intent(OAuthActivity.this, MainActivity.class));
            DocumentReference documentReference = firebaseFirestore.collection("users").document(personName);
            Map<String, Object> user = new HashMap<>();
            user.put("name", personName);
            user.put("picturePath", picturePath);
            user.put("email", personEmail);
            user.put("auth source", "Google");
            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.d("DavidVgn", "onSuccessFirestore: ");
                }
            });
            // Signed in successfully, show authenticated UI.
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("DavidVgn", "signInResult:failed code=" + e.getStatusCode());
        }
    }

}
