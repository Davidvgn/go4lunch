package com.davidvignon.go4lunch.ui.settings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.davidvignon.go4lunch.data.permission.PermissionRepository;
import com.davidvignon.go4lunch.data.preferences.PreferencesRepository;
import com.davidvignon.go4lunch.ui.notification.NotificationWorker;
import com.davidvignon.go4lunch.ui.utils.SingleLiveEvent;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SettingsViewModel extends ViewModel {

    private static final String TAG_NOTIFICATION_WORKER = "TAG_NOTIFICATION_WORKER";

    private final PreferencesRepository preferencesRepository;
    private final PermissionRepository permissionRepository;
    private final WorkManager workManager;
    private final LiveData<Boolean> switchStateLiveData;
    private final SingleLiveEvent<Void> notificationDialogSingleLiveEvent = new SingleLiveEvent<>();


    @Inject
    public SettingsViewModel(
        PreferencesRepository preferencesRepository,
        PermissionRepository permissionRepository,
        WorkManager workManager
    ) {
        this.preferencesRepository = preferencesRepository;
        this.permissionRepository = permissionRepository;
        this.workManager = workManager;

        switchStateLiveData = preferencesRepository.isLunchNotificationEnabledLiveData();
    }

    public LiveData<Boolean> getSwitchValueLiveData() {
        return switchStateLiveData;
    }

    //todo david worker à 12h everyday


    public void onCheckClicked(boolean isEnabled) {
        if (isEnabled) {
            if (!permissionRepository.isNotificationPermissionGranted()) {
                notificationDialogSingleLiveEvent.call();
            } else {
                OneTimeWorkRequest myWorkRequest =
                    new OneTimeWorkRequest.Builder(NotificationWorker.class)
                        .addTag(TAG_NOTIFICATION_WORKER)
                        .build();

                PeriodicWorkRequest workRequestTEST = new PeriodicWorkRequest.Builder(NotificationWorker.class, 1, TimeUnit.DAYS )
                    //  .setInitialDelay(Duration.between()) // TODO Use LocalDate
                    .build();

                preferencesRepository.setLunchNotificationEnabled(true);
                workManager.enqueue(myWorkRequest);
            }
        } else {
            preferencesRepository.setLunchNotificationEnabled(false);
            workManager.cancelAllWorkByTag(TAG_NOTIFICATION_WORKER);
        }
    }

    public SingleLiveEvent<Void> getNotificationDialogSingleLiveEvent() {
        return notificationDialogSingleLiveEvent;
    }


}
