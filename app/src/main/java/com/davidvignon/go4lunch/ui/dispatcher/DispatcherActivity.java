package com.davidvignon.go4lunch.ui.dispatcher;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.davidvignon.go4lunch.ui.main.MainActivity;
import com.davidvignon.go4lunch.ui.oauth.OAuthActivity;

import com.davidvignon.go4lunch.ui.ViewModelFactory;

public class DispatcherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // No "setContentView(int)" to have a fully transparent and performant Activity

        DispatcherViewModel viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(DispatcherViewModel.class);

        viewModel.getViewActionSingleLiveEvent().observe(this, dispatcherViewAction -> {
            switch (dispatcherViewAction) {
                case GO_TO_CONNECT_SCREEN:
                    startActivity(new Intent(DispatcherActivity.this, OAuthActivity.class));
                    finish();
                    break;
                case GO_TO_MAIN_SCREEN:
                    startActivity(new Intent(DispatcherActivity.this, MainActivity.class));
                    finish();
                    break;
            }
        });
    }
}
