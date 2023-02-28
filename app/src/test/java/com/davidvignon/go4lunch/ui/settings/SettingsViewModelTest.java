package com.davidvignon.go4lunch.ui.settings;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
import androidx.work.Operation;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.davidvignon.go4lunch.data.permission.PermissionRepository;
import com.davidvignon.go4lunch.data.preferences.PreferencesRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

public class SettingsViewModelTest {

    private static final String TAG_NOTIFICATION_WORKER = "TAG_NOTIFICATION_WORKER";

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private final PreferencesRepository preferencesRepository = Mockito.mock(PreferencesRepository.class);
    private final PermissionRepository permissionRepository = Mockito.mock(PermissionRepository.class);
    private final WorkManager workManager = Mockito.mock(WorkManager.class);
    private final Operation operation = Mockito.mock(Operation.class);
    MutableLiveData<Boolean> isLunchNotificationEnabledLiveData = new MutableLiveData<>();
    Boolean isNotificationPermissionGranted;
    private SettingsViewModel viewModel;


    @Before
    public void setUp() {
        isNotificationPermissionGranted = true;
        Mockito.doReturn(isNotificationPermissionGranted).when(permissionRepository).isNotificationPermissionGranted();
        Mockito.doReturn(isLunchNotificationEnabledLiveData).when(preferencesRepository).isLunchNotificationEnabledLiveData();
        Mockito.doReturn(operation).when(workManager).enqueue((Mockito.any(PeriodicWorkRequest.class)));

        viewModel = new SettingsViewModel(preferencesRepository, permissionRepository, workManager);

    }

    @Test
    public void if_enabled_is_false_sets_repo_to_false_and_cancel_worker() {
        // When
        viewModel.onCheckClicked(false);

        // Then
        Mockito.verifyNoInteractions(permissionRepository);
        Mockito.verify(preferencesRepository).setLunchNotificationEnabled(false);
        Mockito.verify(workManager).cancelAllWorkByTag(TAG_NOTIFICATION_WORKER);
    }

    @Test
    public void if_enabled_is_true_sets_repo_to_true() {
        // When
        viewModel.onCheckClicked(true);

        // Then
        Mockito.verify(permissionRepository).isNotificationPermissionGranted();
        Mockito.verify(preferencesRepository).setLunchNotificationEnabled(true);


    }

//    @Test
//    public void if_enabled_is_true_with_no_permissions() {//todo david
//        //Given
//        isNotificationPermissionGranted = false;
//
//        // When
//        viewModel.onCheckClicked(true);
//
//        // Then
//        Mockito.verify(permissionRepository).isNotificationPermissionGranted();
//        Mockito.verify(preferencesRepository).setLunchNotificationEnabled(false);
//
//    }
}
