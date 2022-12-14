package com.davidvignon.go4lunch.ui.oauth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class OAuthActivity extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;

    private CallbackManager callbackManager;

    public SignInClient oneTapClient;
    private GoogleSignInClient mGoogleSignInClient;

    public GoogleSignInOptions gso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AuthActivityBinding binding = AuthActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseFirestore = FirebaseFirestore.getInstance();
        callbackManager = CallbackManager.Factory.create();
        oneTapClient = Identity.getSignInClient(this);

        //GOOGLE
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .requestProfile()
            .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        MaterialButton googleLoginButton = binding.authGoogleLoginButton;

        googleLoginButton.setOnClickListener(view -> {
            Log.d("DavidVgn", "googleLoginButton.setOnClickListener");
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

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
        handleSignInResult(task);
    });

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    //FACEBOOK
    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    startActivity(new Intent(OAuthActivity.this, MainActivity.class));
                    Log.d("DavidVgn", "onComplete: isSuccessful");
                } else {
                    Log.d("DavidVgn", "Authentication failed : task : ");

                    Toast.makeText(OAuthActivity.this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show();
                }
            });

        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        GraphRequest request = GraphRequest.newMeRequest(
            accessToken,
            (object, response) -> {
                try {
                    String id = object.getString("id");
                    String fullName = object.getString("name");
                    String email = object.getString("email");
                    String picturePath = object.getJSONObject("picture").getJSONObject("data").getString("url");
                    List<String> favoritesRestaurants = new ArrayList<>();
                    String selectedRestaurant = "";

                    DocumentReference documentReference = firebaseFirestore.collection("users").document(fullName);

                    User user = new User(
                        id,
                        fullName,
                        picturePath,
                        email,
                        favoritesRestaurants,
                        selectedRestaurant
                    );
                    documentReference.set(user).addOnSuccessListener(unused -> Log.d("DavidVgn", "onSuccessFirestore: "));
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
        try {
            GoogleSignInAccount googleAccount = completedTask.getResult(ApiException.class);

            AuthCredential credential = GoogleAuthProvider.getCredential(googleAccount.getIdToken(), null);
            FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {

                    Log.d("DavidVgn", "onComplete: isSuccessful");

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        Log.d("DavidVgn", "user not null");
                        String id = user.getUid();
                        String fullName = user.getDisplayName();
                        String email = user.getEmail();
                        Uri pictureUri = user.getPhotoUrl();
                        String picturePath = pictureUri != null ? pictureUri.toString() : null;

                        if (fullName != null) {

                            Log.d("DavidVgn", "fullName not null");

                            if(email == null) {
                                Log.e("DavidVgn", "email is NULL!");
                            }
                            DocumentReference documentReference = firebaseFirestore.collection("users").document(id);

                            User googleUser = new User(
                                id,
                                fullName,
                                picturePath,
                                email,
                                null,
                                null
                            );

                            documentReference.set(googleUser).addOnSuccessListener(this, unused -> {
                                Log.d("DavidVgn", "onSuccessFirestore: ");

                                startActivity(new Intent(OAuthActivity.this, MainActivity.class));
                            });
                        }
                    }
                } else {
                    Toast.makeText(OAuthActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (ApiException e) {
            Log.w("DavidVgn", "signInResult:failed code=" + e.getStatusCode());
        }
    }
}
