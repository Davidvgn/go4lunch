package com.davidvignon.go4lunch.ui.notification;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
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

    }

    @SuppressLint("MissingPermission")//todo david comment gérer ?
    @NonNull
    @Override
    public Result doWork() {
        Log.d("Dvgn", "Sending notification");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Name";
            String description = "Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notifManager = getApplicationContext().getSystemService(NotificationManager.class);
            notifManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setContentTitle(getApplicationContext().getString(R.string.app_name))
            .setStyle(new NotificationCompat.BigTextStyle()
                .bigText(getnotificationMessage()))
            .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

        notificationManager.notify(0, builder.build());

        return Result.success();
    }

    public String getnotificationMessage() {

        String notificationMessage;
        String workmatesList = "";
        String hereYouEat;

        List<String> workmateNameList = new ArrayList<>();
        if (notificationRepository.getRestaurantNameLiveData().getValue() != null) {
            String restaurantId = notificationRepository.getRestaurantPlaceIdLiveData().getValue();
            List<Workmate> workmateGoingThereList = notificationRepository.getUserListGoingToLiveData(restaurantId).getValue();

            if (workmateGoingThereList != null) {
                for (Workmate workmate : workmateGoingThereList) {
                    if (!workmate.getId().equals(firebaseAuth.getCurrentUser().getUid())) {
                        workmateNameList.add(workmate.getName() + "\n");
                    }
                }
                if (!workmateNameList.isEmpty()) {
                    workmatesList = "Ce(s) collègue(s) mange(nt) ici également : \n"
                        + "- " + workmateNameList.toString().replace("[","").replace("]","");
                }
            }

            hereYouEat = "Vous avez prévu de manger ici : " + notificationRepository.getRestaurantNameLiveData().getValue() + "\n" + "\n";
            notificationMessage = hereYouEat + workmatesList;

        } else {
            notificationMessage = "Aucun restaurant sélectionné aujourd'hui ";
        }
        return notificationMessage;
    }
}
