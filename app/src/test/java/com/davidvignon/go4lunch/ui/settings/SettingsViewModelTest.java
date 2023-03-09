package com.davidvignon.go4lunch.ui.settings;


import static org.mockito.ArgumentMatchers.any;

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
    private final MutableLiveData<Boolean> isLunchNotificationEnabledLiveData = new MutableLiveData<>();
    private SettingsViewModel viewModel;


    @Before
    public void setUp() {
        Mockito.doReturn(true).when(permissionRepository).isNotificationPermissionGranted();
        Mockito.doReturn(isLunchNotificationEnabledLiveData).when(preferencesRepository).isLunchNotificationEnabledLiveData();
        Mockito.doReturn(operation).when(workManager).enqueue((any(PeriodicWorkRequest.class)));

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
    public void if_enabled_sets_repo_to_true() {
        // When
        viewModel.onCheckClicked(true);

        // Then
        Mockito.verify(permissionRepository).isNotificationPermissionGranted();
        Mockito.verify(preferencesRepository).setLunchNotificationEnabled(true);
        Mockito.verify(workManager).enqueueUniquePeriodicWork(any(), any(), any());
    }

    @Test
    public void if_enabled_without_permissions() {
        //Given
        Mockito.doReturn(false).when(permissionRepository).isNotificationPermissionGranted();

        // When
        viewModel.onCheckClicked(true);

        // Then
        Mockito.verify(permissionRepository).isNotificationPermissionGranted();
        Mockito.verify(preferencesRepository, Mockito.never()).setLunchNotificationEnabled(true);
        Mockito.verify(workManager, Mockito.never()).enqueueUniquePeriodicWork(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    public void if_not_enabled_repo_is_set_to_false_and_workManager_is_cancel() {
        // When
        viewModel.onCheckClicked(false);

        // Then
        Mockito.verify(preferencesRepository).setLunchNotificationEnabled(false);
        Mockito.verify(workManager).cancelAllWorkByTag(any());
    }
}
