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
import androidx.lifecycle.MutableLiveData;

import com.davidvignon.go4lunch.R;
import com.davidvignon.go4lunch.data.users.User;
import com.davidvignon.go4lunch.data.users.UserRepository;
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

import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class OAuthActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

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

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
        handleSignInResult(task);
    });

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    //FACEBOOK
    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
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
            String id = googleAccount.getId();

            Log.d("DavidVgn", "handleSignInResult: " + id);
            String fullName = googleAccount.getDisplayName();
            String email = googleAccount.getEmail();
            Uri pictureUri = googleAccount.getPhotoUrl();
            String picturePath = pictureUri.toString();
            List<String> favoritesRestaurants = new ArrayList<>();
            String selectedRestaurant = "";

            String idToken = googleAccount.getIdToken();
            Log.d("DavidVgn", "idtoken: " + idToken);


            AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
            mAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    startActivity(new Intent(OAuthActivity.this, MainActivity.class));
                    Log.d("DavidVgn", "onComplete: isSuccessful");
                } else {
                    Toast.makeText(OAuthActivity.this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show();
                }
            });

            DocumentReference documentReference = firebaseFirestore.collection("users").document(fullName);

            User googleUser = new User(
                id,
                fullName,
                picturePath,
                email,
                favoritesRestaurants,
                selectedRestaurant
            );

            documentReference.set(googleUser).addOnSuccessListener(this, unused -> Log.d("DavidVgn", "onSuccessFirestore: "));

        } catch (ApiException e) {
            Log.w("DavidVgn", "signInResult:failed code=" + e.getStatusCode());
        }
    }
}
