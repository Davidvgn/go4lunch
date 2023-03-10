package com.davidvignon.go4lunch.ui.dispatcher;

import static org.junit.Assert.assertEquals;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.davidvignon.go4lunch.utils.LiveDataTestUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

public class DispatcherViewModelTest {

    private static String DEFAULT_USER_ID;
    private static String DEFAULT_USER_NAME;
    private static String DEFAULT_USER_EMAIL;


    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private final FirebaseAuth firebaseAuth = Mockito.mock(FirebaseAuth.class);
    private final FirebaseUser firebaseUser = Mockito.mock(FirebaseUser.class);

    private DispatcherViewModel viewModel;

    @Test
    public void if_current_user_not_null_go_to_main_screen() {
        // Given
        DEFAULT_USER_NAME = "DEFAULT_USER_NAME";
        DEFAULT_USER_ID = "DEFAULT_USER_ID";
        DEFAULT_USER_EMAIL = "DEFAULT_USER_EMAIL";
        Mockito.doReturn(DEFAULT_USER_NAME).when(firebaseUser).getDisplayName();
        Mockito.doReturn(DEFAULT_USER_ID).when(firebaseUser).getUid();
        Mockito.doReturn(DEFAULT_USER_EMAIL).when(firebaseUser).getEmail();
        Mockito.doReturn(firebaseUser).when(firebaseAuth).getCurrentUser();
        viewModel = new DispatcherViewModel(firebaseAuth);

        // When
        DispatcherViewAction viewAction = LiveDataTestUtils.getValueForTesting(viewModel.getViewActionSingleLiveEvent());

        // Then
        assertEquals(viewAction.toString(), "GO_TO_MAIN_SCREEN");
    }

    @Test
    public void if_current_user_is_null_go_to_connect_screen() {
        // Given
        DEFAULT_USER_ID = null;
        DEFAULT_USER_NAME = null;
        DEFAULT_USER_EMAIL = null;

        // When
        Mockito.doReturn(DEFAULT_USER_NAME).when(firebaseUser).getDisplayName();
        Mockito.doReturn(DEFAULT_USER_ID).when(firebaseUser).getUid();
        Mockito.doReturn(DEFAULT_USER_EMAIL).when(firebaseUser).getEmail();
        Mockito.doReturn(null).when(firebaseAuth).getCurrentUser();
        viewModel = new DispatcherViewModel(firebaseAuth);

        DispatcherViewAction viewAction = LiveDataTestUtils.getValueForTesting(viewModel.getViewActionSingleLiveEvent());

        // Then
        assertEquals(viewAction.toString(), "GO_TO_CONNECT_SCREEN");
    }
}
