package com.davidvignon.go4lunch.ui.settings;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.davidvignon.go4lunch.data.permission.PermissionRepository;
import com.davidvignon.go4lunch.data.preferences.PreferencesRepository;
import com.davidvignon.go4lunch.ui.notification.NotificationWorker;
import com.davidvignon.go4lunch.ui.utils.SingleLiveEvent;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SettingsViewModel extends ViewModel {
    //todo david faire test unit

    private static final String TAG_NOTIFICATION_WORKER = "TAG_NOTIFICATION_WORKER";

    private final PreferencesRepository preferencesRepository;
    private final PermissionRepository permissionRepository;
    private final WorkManager workManager;
    private final LiveData<Boolean> switchStateLiveData;
    private final SingleLiveEvent<Void> notificationDialogSingleLiveEvent = new SingleLiveEvent<>();


    @Inject
    public SettingsViewModel(PreferencesRepository preferencesRepository, PermissionRepository permissionRepository, WorkManager workManager) {
        this.preferencesRepository = preferencesRepository;
        this.permissionRepository = permissionRepository;
        this.workManager = workManager;

        switchStateLiveData = preferencesRepository.isLunchNotificationEnabledLiveData();
    }

    public LiveData<Boolean> getSwitchValueLiveData() {
        return switchStateLiveData;
    }

    public void onCheckClicked(boolean isEnabled) {
        if (isEnabled) {
            if (!permissionRepository.isNotificationPermissionGranted()) {
                notificationDialogSingleLiveEvent.call();
            } else {


                LocalDateTime now = LocalDateTime.now();
                LocalDateTime targetTime = now.with(LocalTime.NOON);
                if (now.compareTo(targetTime) > 0) {
                    targetTime = targetTime.plusDays(1);
                }
                Duration duration = Duration.between(now, targetTime);
                long delayMillis = duration.toMillis();

                PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(
                    NotificationWorker.class, 1, TimeUnit.DAYS)
                    .addTag(TAG_NOTIFICATION_WORKER)
                    .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS).build();
//
//                OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class)//todo david pour test touch notif
//                    .addTag(TAG_NOTIFICATION_WORKER)
//                    .build();

                preferencesRepository.setLunchNotificationEnabled(true);
                workManager.enqueue(workRequest);
            }
        } else {
            preferencesRepository.setLunchNotificationEnabled(false);
            workManager.cancelAllWorkByTag(TAG_NOTIFICATION_WORKER);
        }
    }

    public SingleLiveEvent<Void> getNotificationDialogSingleLiveEvent() {
        return notificationDialogSingleLiveEvent;
    }

    public Boolean getSwitchValue() {
        return preferencesRepository.getSwitchValue();
    }

}
