package com.davidvignon.go4lunch.ui.oauth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.davidvignon.go4lunch.R;
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
import com.google.android.gms.auth.api.signin.GoogleSignIn;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class OAuthActivity extends AppCompatActivity {

    AuthActivityBinding binding;

    private FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore;

    CallbackManager callbackManager;

    public SignInClient oneTapClient;
    private GoogleSignInClient mGoogleSignInClient;

    public GoogleSignInOptions gso;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AuthActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseFirestore = FirebaseFirestore.getInstance();
        callbackManager = CallbackManager.Factory.create();
        oneTapClient = Identity.getSignInClient(this);

        mAuth = FirebaseAuth.getInstance();

        //GOOGLE
         gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        MaterialButton googleLoginButton = binding.authGoogleLoginButton;

        googleLoginButton.setOnClickListener(view -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            activityResultLauncher.launch(signInIntent);
        });

        //FACEBOOK
        LoginButton facebookButton = binding.authFacebookLoginButton;

        facebookButton.setReadPermissions("email", "public_profile", "user_photos");

        facebookButton.registerCallback(callbackManager,
            new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    handleFacebookAccessToken(loginResult.getAccessToken());
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

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
            handleSignInResult(task);
        }
    });

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }


    private void handleFacebookAccessToken(AccessToken token) {
//        mGoogleSignInClient.signOut()
//            .addOnCompleteListener(this, new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    Log.d("DavidVgn", "Google signOut() ");
//                }
//            });

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(OAuthActivity.this, MainActivity.class));

                        Log.d("DavidVgn", "onComplete: isSuccessful");
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.d("DavidVgn", "Authentication failed : task : ");

                        Toast.makeText(OAuthActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    }
                }
            });
        //FACEBOOK
        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        GraphRequest request = GraphRequest.newMeRequest(
            accessToken,
            (object, response) -> {
                // Application code
                try {
                    String id = object.getString("id");
                    String fullName = object.getString("name");
                    String email = object.getString("email");
                    String picturePath = object.getJSONObject("picture").getJSONObject("data").getString("url");

                    DocumentReference documentReference = firebaseFirestore.collection("users").document(fullName);

                    User user = new User(
                        id,
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
        parameters.putString("fields", "id, name, email, link,picture.type(large)");
        request.setParameters(parameters);
        request.executeAsync();


    }


    //GOOGLE
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
//        LoginManager.getInstance().logOut();
        try {
        GoogleSignInAccount acct = completedTask.getResult(ApiException.class);
            String id = acct.getId();
            String fullName = acct.getDisplayName();
            String email = acct.getEmail();
            Uri pictureUri = acct.getPhotoUrl();
            String picturePath = pictureUri.toString();

            String idToken = acct.getIdToken();

            AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
            mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(OAuthActivity.this, MainActivity.class));
                        Log.d("DavidVgn", "onComplete: isSuccessful");
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(OAuthActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    }
                }
            });

            DocumentReference documentReference = firebaseFirestore.collection("users").document(fullName);

            User googleUser = new User(
                id,
                fullName,
                picturePath,
                email,
                "Google"
            );

            documentReference.set(googleUser).addOnSuccessListener(this, new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.d("DavidVgn", "onSuccessFirestore: ");
                }
            });
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("DavidVgn", "signInResult:failed code=" + e.getStatusCode());
        }


//        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(OAuthActivity.this);
//        String id = acct.getId();
//        String fullName = acct.getDisplayName();
//        String email = acct.getEmail();
//        Uri pictureUri = acct.getPhotoUrl();
//        String picturePath = pictureUri.toString();
//        try {
//            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
//            startActivity(new Intent(OAuthActivity.this, MainActivity.class));
//            DocumentReference documentReference = firebaseFirestore.collection("users").document(fullName);
//
//            User googleUser = new User(
//                id,
//                fullName,
//                picturePath,
//                email,
//                "Google"
//            );
//
//            documentReference.set(googleUser).addOnSuccessListener(new OnSuccessListener<Void>() {
//                @Override
//                public void onSuccess(Void unused) {
//                    Log.d("DavidVgn", "onSuccessFirestore: ");
//                }
//            });
//            // Signed in successfully, show authenticated UI.
//        } catch (ApiException e) {
//            // The ApiException status code indicates the detailed failure reason.
//            // Please refer to the GoogleSignInStatusCodes class reference for more information.
//            Log.w("DavidVgn", "signInResult:failed code=" + e.getStatusCode());
//        }
    }
}
