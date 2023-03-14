package com.davidvignon.go4lunch.data.preferences;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.davidvignon.go4lunch.utils.LiveDataTestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

public class PreferencesRepositoryTest {

    private static final String SWITCH_KEY = "SWITCH_KEY";
    private static final String SHARED_PREFERENCES = "SHARED_PREFERENCES";

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    private final Application application = Mockito.mock(Application.class);
    private final SharedPreferences sharedPreferences = Mockito.mock(SharedPreferences.class);
    private final SharedPreferences.Editor sharedPreferencesEditor = Mockito.mock(SharedPreferences.Editor.class);

    private PreferencesRepository preferencesRepository;

    @Before
    public void Setup() {

        Mockito.doReturn(sharedPreferences).when(application).getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        Mockito.doReturn(sharedPreferencesEditor).when(sharedPreferences).edit();

        Mockito.doReturn(sharedPreferencesEditor).when(sharedPreferencesEditor).putBoolean(any(), anyBoolean());

        Mockito.doNothing().when(sharedPreferencesEditor).apply();

        preferencesRepository = new PreferencesRepository(application);
    }

    @Test
    public void if_true_and_then_set_to_false_it_returns_false() {
        // Given
        boolean sharedValue = false;
        Mockito.doReturn(sharedValue).when(sharedPreferences).getBoolean(SWITCH_KEY, false);

        // when
        sharedValue = true;
        preferencesRepository.setLunchNotificationEnabled(!sharedValue);
        Boolean sharedPreferencesValue = LiveDataTestUtils.getValueForTesting(preferencesRepository.isLunchNotificationEnabledLiveData());

        // then
        assertFalse(sharedPreferencesValue);
    }


    @Test
    public void if_set_to_true_sharedPreferences_is_true() {
        // Given
        Boolean sharedValue = true;
        Mockito.doReturn(sharedValue).when(sharedPreferences).getBoolean(SWITCH_KEY, false);

        // When
        Boolean sharedPreferencesValue = LiveDataTestUtils.getValueForTesting(preferencesRepository.isLunchNotificationEnabledLiveData());

        // Then
        assertTrue(sharedPreferencesValue);
    }

    @Test
    public void if_set_to_false_sharedPreferences_is_false() {
        // Given
        Boolean sharedValue = false;
        Mockito.doReturn(sharedValue).when(sharedPreferences).getBoolean(SWITCH_KEY, false);

        // When
        Boolean sharedPreferencesValue = LiveDataTestUtils.getValueForTesting(preferencesRepository.isLunchNotificationEnabledLiveData());

        // Then
        assertFalse(sharedPreferencesValue);
    }
}
