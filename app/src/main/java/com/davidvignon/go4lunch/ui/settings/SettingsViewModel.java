package com.davidvignon.go4lunch.ui.settings;

import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.davidvignon.go4lunch.data.SettingsRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SettingsViewModel extends ViewModel {
    private static final String SWITCH_KEY = "SWITCH_KEY";
    private final SharedPreferences sharedPreferences;
    private final MutableLiveData<Boolean> switchStateMutableLiveData = new MutableLiveData<>();
    private final MediatorLiveData<Boolean> mediatorLiveData = new MediatorLiveData<>();


    @Inject
    public SettingsViewModel(
        SharedPreferences sharedPreferences,
        SettingsRepository settingsRepository
    ) {
        this.sharedPreferences = sharedPreferences;

        switchStateMutableLiveData.setValue(settingsRepository.getSwitchValue());

        mediatorLiveData.addSource(switchStateMutableLiveData, switchValue -> combine(switchValue));
    }

    public void combine(Boolean switchValue) {
        if (switchValue == null) {
            switchValue = false;
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SWITCH_KEY, switchValue);
        editor.apply();

        mediatorLiveData.setValue(switchValue);

    }

    public LiveData<Boolean> getSwitchValueLiveData() {
        return mediatorLiveData;
    }

    public void getNewSwitchValue(boolean value) {
        switchStateMutableLiveData.setValue(value);
    }
}
