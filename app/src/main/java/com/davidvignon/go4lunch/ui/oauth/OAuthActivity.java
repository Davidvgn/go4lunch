package com.davidvignon.go4lunch.ui.oauth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.davidvignon.go4lunch.R;
import com.davidvignon.go4lunch.databinding.AuthActivityBinding;
import com.davidvignon.go4lunch.ui.main.MainActivity;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class OAuthActivity extends AppCompatActivity {

    private CallbackManager callbackManager;

    private OAuthViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AuthActivityBinding binding = AuthActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(OAuthViewModel.class);

        callbackManager = CallbackManager.Factory.create();

        //GOOGLE
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .requestProfile()
            .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        binding.authGoogleLoginButton.setOnClickListener(view -> {
            Log.i("OAuthActivity", "googleLoginButton.setOnClickListener");
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            activityResultLauncher.launch(signInIntent);
        });

        //FACEBOOK
        binding.authFacebookLoginButton.setPermissions("email", "public_profile", "user_photos");

        binding.authFacebookLoginButton.registerCallback(callbackManager,
            new FacebookCallback<>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    viewModel.getFacebookUserInfo(loginResult.getAccessToken()).observe(OAuthActivity.this, user -> startActivity(new Intent(OAuthActivity.this, MainActivity.class)));
                }

                @Override
                public void onCancel() {
                    Log.i("OAuthActivity", "onCancel: ");
                }

                @Override
                public void onError(@NonNull FacebookException exception) {
                    Log.i("OAuthActivity","onError: " + exception);
                }
            });
    }

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
        viewModel.getGoogleUserInfo(task).observe(OAuthActivity.this, user -> startActivity(new Intent(OAuthActivity.this, MainActivity.class)));
    });

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}