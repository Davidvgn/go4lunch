package com.davidvignon.go4lunch.ui.notification;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.hilt.work.HiltWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;

@HiltWorker
public class NotificationWorker extends Worker {

    private static final String CHANNEL_ID = "CHANNEL_ID";

    @AssistedInject
    NotificationWorker(
        @Assisted @NonNull Context context,
        @Assisted @NonNull WorkerParameters params
    ) {
        super(context, params);

    }

    @SuppressLint("MissingPermission")
    @NonNull
    @Override
    public Result doWork() {
        Log.d("NotificationWorker", "Sending notification");


        return Result.success();
    }
}