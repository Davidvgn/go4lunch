package com.davidvignon.go4lunch.data;

import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SettingsRepository {
    private static final String SWITCH_KEY = "SWITCH_KEY";
    private final SharedPreferences sharedPreferences;

    @Inject
    public SettingsRepository(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public Boolean getSwitchValue() {
        return sharedPreferences.getBoolean(SWITCH_KEY, false);
    }

}
