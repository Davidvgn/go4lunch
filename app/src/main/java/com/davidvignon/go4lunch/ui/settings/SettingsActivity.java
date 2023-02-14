package com.davidvignon.go4lunch.ui.settings;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.davidvignon.go4lunch.R;
import com.davidvignon.go4lunch.databinding.NotificationActivityBinding;
import com.davidvignon.go4lunch.ui.notification.NotificationWorker;

import java.util.concurrent.TimeUnit;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class SettingsActivity extends AppCompatActivity {

    //a faire dans worker de manière synchrone car il est déjà sur un background thread (donc pas de onCompleteListener)
    //todo david notif :  requeter en BDD quel workmate va dans quel resto
    //todo david : est ce que mon user a choisi un resto sinon lui dire qu'il n'a pas choisi de resto
    //todo david si user désactive switch -> désactiver worker

    private static final String SWITCH_KEY = "SWITCH_KEY";
    private static final String SHARED_PREFERENCES = "SHARED_PREFERENCES";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NotificationActivityBinding binding = NotificationActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SettingsViewModel viewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);

        binding.notificationSw.setChecked((sharedPreferences.getBoolean(SWITCH_KEY, false)));

        viewModel.getSwitchValueLiveData().observe(this, aBoolean -> binding.notificationSw.setOnCheckedChangeListener((compoundButton, newSwitchValue) -> {
            viewModel.getNewSwitchValue(newSwitchValue);
            test();//todo david
        }));
    }

    @SuppressLint("MissingPermission")
    public void test() {//todo david
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "name";
            String description = "descript";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", name, importance);
            channel.setDescription(description);
            NotificationManager notifManager = getApplicationContext().getSystemService(NotificationManager.class);
            notifManager.createNotificationChannel(channel);
        }


        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "CHANNEL_ID")
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setContentTitle("My notification")
            .setContentText("Much longer text that cannot fit one line...")
            .setStyle(new NotificationCompat.BigTextStyle()
                .bigText("Much longer text that cannot fit one line..."))
            .setPriority(NotificationCompat.PRIORITY_HIGH);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

        notificationManager.notify(0, builder.build());

        OneTimeWorkRequest myWorkRequest =
            new OneTimeWorkRequest.Builder(NotificationWorker.class)
                .setInitialDelay(30, TimeUnit.SECONDS)
                .build();

        WorkManager.getInstance(getApplicationContext()).enqueue(myWorkRequest);
//
    }

}
