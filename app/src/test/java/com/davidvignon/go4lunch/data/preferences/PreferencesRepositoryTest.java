package com.davidvignon.go4lunch.data.preferences;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

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
    public void Setup(){
        Mockito.doReturn(sharedPreferences).when(application).getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);


        Mockito.doReturn(sharedPreferencesEditor).when(sharedPreferences).edit();

        Mockito.doReturn(sharedPreferencesEditor).when(sharedPreferencesEditor).putBoolean(any(), anyBoolean());
        Mockito.doNothing().when(sharedPreferencesEditor).apply();

        preferencesRepository = new PreferencesRepository(application);

    }


    @Test
    public void test(){

    }
}
