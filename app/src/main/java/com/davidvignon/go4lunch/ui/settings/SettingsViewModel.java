package com.davidvignon.go4lunch.ui.settings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.davidvignon.go4lunch.data.permission.PermissionRepository;
import com.davidvignon.go4lunch.data.preferences.PreferencesRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SettingsViewModel extends ViewModel {
    private final PreferencesRepository preferencesRepository;
    private final PermissionRepository permissionRepository;
    private final LiveData<Boolean> switchStateLiveData;

    @Inject
    public SettingsViewModel(
        PreferencesRepository preferencesRepository,
        PermissionRepository permissionRepository
    ) {
        this.preferencesRepository = preferencesRepository;
        this.permissionRepository = permissionRepository;

        switchStateLiveData = preferencesRepository.isLunchNotificationEnabledLiveData();
    }

    public LiveData<Boolean> getSwitchValueLiveData() {
        return switchStateLiveData;
    }

    public void onCheckClicked(boolean isEnabled) {
        if (isEnabled) {
            if (!permissionRepository.isNotificationPermissionGranted()) {
                // TODO DAVID ASK Notification permission here !
            } else {
                preferencesRepository.setLunchNotificationEnabled(true);
            }
        } else {
            preferencesRepository.setLunchNotificationEnabled(false);
        }
    }
}
