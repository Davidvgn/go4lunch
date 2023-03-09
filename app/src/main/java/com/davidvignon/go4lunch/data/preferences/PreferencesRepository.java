package com.davidvignon.go4lunch.data.preferences;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PreferencesRepository {
    private static final String SWITCH_KEY = "SWITCH_KEY";
    private static final String SHARED_PREFERENCES = "SHARED_PREFERENCES";

    private final SharedPreferences sharedPreferences;

    @Inject
    public PreferencesRepository(@NonNull Application application) {
        sharedPreferences = application.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
    }

    public void setLunchNotificationEnabled(boolean enabled) {
        sharedPreferences.edit().putBoolean(SWITCH_KEY, enabled).apply();
    }

    public LiveData<Boolean> isLunchNotificationEnabledLiveData() {
        MutableLiveData<Boolean> isLunchNotificationEnabledMutableLiveData = new MutableLiveData<>();

        isLunchNotificationEnabledMutableLiveData.setValue(sharedPreferences.getBoolean(SWITCH_KEY, false));

        SharedPreferences.OnSharedPreferenceChangeListener listener = (sharedPreferences, key) -> {
            if (key.equals(SWITCH_KEY)) {
                isLunchNotificationEnabledMutableLiveData.setValue(sharedPreferences.getBoolean(SWITCH_KEY, false));
            }
        };

        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);

        return isLunchNotificationEnabledMutableLiveData;
    }
}