package com.davidvignon.go4lunch.ui.oauth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;

public class OAuthActivity extends AppCompatActivity {

    AuthActivityBinding binding;

    private static final int REQ_ONE_TAP = 2;
    private static final int RC_SIGN_IN = 3;
    private boolean showOneTapUI = true;

    private FirebaseAuth mAuth;
    CallbackManager callbackManager;

    private SignInClient oneTapClient;
    private GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AuthActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //GOOGLE
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        // Set the dimensions of the sign-in button.
        SignInButton gooleSignInButton = binding.authGoogleLoginButton;
        gooleSignInButton.setSize(SignInButton.SIZE_STANDARD);

        gooleSignInButton.setOnClickListener(view -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });


        //FACEBOOK
        LoginButton facebookButton = binding.authFacebookLoginButton;

        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        callbackManager = CallbackManager.Factory.create();

        mAuth = FirebaseAuth.getInstance();

        oneTapClient = Identity.getSignInClient(this);

        facebookButton.setReadPermissions("email", "public_profile", "user_photos");

        GraphRequest request = GraphRequest.newMeRequest(
            accessToken,
            (object, response) -> {
                // Application code
                try {
                    String fullName = object.getString("name");
                    String url = object.getJSONObject("picture").getJSONObject("data").getString("url");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,picture.type(large)");
        request.setParameters(parameters);
        request.executeAsync();

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

                    // App code
                }

                @Override
                public void onError(FacebookException exception) {
                    Log.d("DavidVgn", "onError: " + exception);

                    // App code
                }
            });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

                //GOOGLE
                // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
            case RC_SIGN_IN:
                // The Task returned from this call is always completed, no need to attach
                // a listener.
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

    @Override
    public void onStart() {
        super.onStart();
        //-----FACEBOOK
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        facebookUpdateUI(currentUser);

        //------GOOGLE
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        googleUpdateUI(account);

    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    Log.d("DavidVgn", "onComplete: " + task);
                    // Sign in success, update UI with the signed-in user's information
                    FirebaseUser user = mAuth.getCurrentUser();
                    facebookUpdateUI(user);
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(OAuthActivity.this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show();
                    facebookUpdateUI(null);
                }
            });
    }

    //GOOGLE
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            startActivity(new Intent(OAuthActivity.this, MainActivity.class));
            // Signed in successfully, show authenticated UI.
            googleUpdateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("DavidVgn", "signInResult:failed code=" + e.getStatusCode());
            googleUpdateUI(null);
        }
    }


    private void facebookUpdateUI(FirebaseUser user) {
    }

    private void googleUpdateUI(GoogleSignInAccount account) {
    }

}
