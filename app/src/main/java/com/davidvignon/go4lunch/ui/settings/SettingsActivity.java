package com.davidvignon.go4lunch.ui.settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.CompoundButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.davidvignon.go4lunch.databinding.NotificationActivityBinding;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NotificationActivityBinding binding = NotificationActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        SettingsViewModel viewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        binding.notificationSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean newCheckValue) {
                viewModel.onCheckClicked(newCheckValue);
            }
        });

        //noinspection Convert2MethodRef
        viewModel.getSwitchValueLiveData().observe(this, checked -> binding.notificationSw.setChecked(checked));

        viewModel.getNotificationDialogSingleLiveEvent().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void unused) {
                new AlertDialog.Builder(SettingsActivity.this)
                    .setTitle("Notifications disabled")// todo david  all text in strings
                    .setMessage("Please enable notifications in the settings to receive notification.")
                    .setPositiveButton("Allow notification", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                                intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                                startActivity(intent);
                            }
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            binding.notificationSw.setChecked(false);
                        }
                    })
                    .setCancelable(false)
                    .create()
                    .show();
            }
        });
    }
}
