package com.davidvignon.go4lunch.ui.notification;

import android.Manifest;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.core.content.ContextCompat;
import androidx.hilt.work.HiltWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.davidvignon.go4lunch.R;
import com.davidvignon.go4lunch.data.NotificationRepository;
import com.davidvignon.go4lunch.data.workmate.Workmate;
import com.davidvignon.go4lunch.ui.details.RestaurantDetailsViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;

@HiltWorker
public class NotificationWorker extends Worker {

    private static final String CHANNEL_ID = "CHANNEL_ID";

    private final Application application;
    private final NotificationRepository notificationRepository;
    private final FirebaseAuth firebaseAuth;
    private final NotificationManagerCompat notificationManager;

    @AssistedInject
    NotificationWorker(
        @Assisted @NonNull Context context,
        @Assisted @NonNull WorkerParameters params,
        Application application,
        NotificationRepository notificationRepository,
        FirebaseAuth firebaseAuth
    ) {
        super(context, params);

        this.application = application;
        this.notificationRepository = notificationRepository;
        this.firebaseAuth = firebaseAuth;
        this.notificationManager = NotificationManagerCompat.from(context);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d("Dvgn", "Sending notification");
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, application.getString(R.string.notification_name), NotificationManager.IMPORTANCE_DEFAULT);
                channel.setDescription(application.getString(R.string.notification_description));
                notificationManager.createNotificationChannel(channel);
            }

            Intent intent = RestaurantDetailsViewModel.navigate(getApplicationContext(), notificationRepository.getRestaurantPlaceId())
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            PendingIntent pendingIntent = TaskStackBuilder.create(getApplicationContext())
                .addNextIntentWithParentStack(intent)
                .getPendingIntent(
                    0,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                );

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setContentTitle(getApplicationContext().getString(R.string.app_name))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(getNotificationMessage()))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

            notificationManager.notify(0, builder.build());
        }

        return Result.success();
    }

    public String getNotificationMessage() {

        String notificationMessage;
        String workmatesList = "";
        String hereYouEat;

        String restaurantName = notificationRepository.getRestaurantName();

        List<String> workmateNameList = new ArrayList<>();
        if (restaurantName != null) {
            String restaurantId = notificationRepository.getRestaurantPlaceId();
            List<Workmate> workmateGoingThereList = notificationRepository.getUserListWill(restaurantId);

            if (workmateGoingThereList != null) {
                for (Workmate workmate : workmateGoingThereList) {
                    if (!workmate.getId().equals(firebaseAuth.getCurrentUser().getUid())) {
                        workmateNameList.add(workmate.getName() + "\n");
                    }
                }
                if (!workmateNameList.isEmpty()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("\n\n")
                        .append(application.getString(R.string.notification_workmates_list))
                        .append("\n");
                    for (String workmateName : workmateNameList) {
                        sb.append("- ")
                            .append(workmateName)
                            .append("\n");
                    }
                    workmatesList = sb.toString();
                }
            }

            hereYouEat = application.getString(R.string.here_you_eat_notification) + restaurantName;
            notificationMessage = hereYouEat + workmatesList;

        } else {
            notificationMessage = application.getString(R.string.no_restaurant_selected_notification);
        }
        return notificationMessage;
    }
}
