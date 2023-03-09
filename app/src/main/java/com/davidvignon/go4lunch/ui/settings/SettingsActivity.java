package com.davidvignon.go4lunch.ui.settings;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.davidvignon.go4lunch.R;
import com.davidvignon.go4lunch.databinding.NotificationActivityBinding;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NotificationActivityBinding binding = NotificationActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.notificationToolbar);


        SettingsViewModel viewModel = new ViewModelProvider(this).get(SettingsViewModel.class);


        binding.notificationSw.setOnCheckedChangeListener((compoundButton, newCheckValue) -> viewModel.onCheckClicked(newCheckValue));

        //noinspection Convert2MethodRef
        viewModel.getSwitchValueLiveData().observe(this, checked -> binding.notificationSw.setChecked(checked));

        viewModel.getNotificationDialogSingleLiveEvent().observe(this, unused -> new AlertDialog.Builder(SettingsActivity.this)
            .setTitle(R.string.notifications_disabled)
            .setMessage(R.string.please_enable_notifications)
            .setPositiveButton(R.string.allow_notification, (dialog, which) -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                    intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                    startActivity(intent);
                }
            })
            .setNegativeButton(R.string.cancel, (dialogInterface, i) -> binding.notificationSw.setChecked(false))
            .setCancelable(false)
            .create()
            .show());
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
