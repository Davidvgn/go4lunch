package com.davidvignon.go4lunch.ui.notification;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.hilt.work.HiltWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.davidvignon.go4lunch.R;
import com.davidvignon.go4lunch.data.NotificationRepository;
import com.davidvignon.go4lunch.data.workmate.Workmate;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;

@HiltWorker
public class NotificationWorker extends Worker {

    private static final String CHANNEL_ID = "CHANNEL_ID";

    private final NotificationRepository notificationRepository;
    private final FirebaseAuth firebaseAuth;
    private final NotificationManagerCompat notificationManager;


    @AssistedInject
    NotificationWorker(
        @Assisted @NonNull Context context,
        @Assisted @NonNull WorkerParameters params,
        NotificationRepository notificationRepository,
        FirebaseAuth firebaseAuth
    ) {
        super(context, params);

        this.notificationRepository = notificationRepository;
        this.firebaseAuth = firebaseAuth;
        this.notificationManager = NotificationManagerCompat.from(getApplicationContext());
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d("Dvgn", "Sending notification");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Name"; // TODO David strings
            String description = "Description";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setContentTitle(getApplicationContext().getString(R.string.app_name))
            .setStyle(new NotificationCompat.BigTextStyle().bigText(getNotificationMessage()))
            .setPriority(NotificationCompat.PRIORITY_HIGH);

        // TODO David Au click notification, aller sur le detail

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify(0, builder.build());
            return Result.success();
        }

        return Result.failure();
    }

    public String getNotificationMessage() {

        String notificationMessage;
        String workmatesList = "";
        String hereYouEat;

        String restaurantName = notificationRepository.getRestaurantName();

        List<String> workmateNameList = new ArrayList<>();
        if (restaurantName != null) {
            String restaurantId = notificationRepository.getRestaurantPlaceIdLiveData().getValue();
            List<Workmate> workmateGoingThereList = notificationRepository.getUserListGoingToLiveData(restaurantId).getValue();

            if (workmateGoingThereList != null) {
                for (Workmate workmate : workmateGoingThereList) {
                    if (!workmate.getId().equals(firebaseAuth.getCurrentUser().getUid())) {
                        workmateNameList.add(workmate.getName() + "\n");
                    }
                }
                if (!workmateNameList.isEmpty()) {
                    workmatesList =  "\n" + "\n" + "Ce(s) collègue(s) mange(nt) ici également : \n" // TODO DAVID String + StringBuilder
                        + "- " + workmateNameList.toString().replace("[", "").replace("]", "");
                }
            }

            hereYouEat = "Vous avez prévu de manger ici : " + restaurantName;
            notificationMessage = hereYouEat + workmatesList;

        } else {
            notificationMessage = "Aucun restaurant sélectionné aujourd'hui ";
        }
        return notificationMessage;
    }
}
