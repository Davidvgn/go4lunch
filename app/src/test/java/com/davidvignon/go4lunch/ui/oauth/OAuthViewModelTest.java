package com.davidvignon.go4lunch.ui.oauth;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.davidvignon.go4lunch.data.OAuthRepository;
import com.davidvignon.go4lunch.data.users.User;
import com.davidvignon.go4lunch.utils.LiveDataTestUtils;
import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import okhttp3.internal.concurrent.Task;

public class OAuthViewModelTest {

    private final static String DEFAULT_ID_1 = "DEFAULT_ID_1";
    private final static String DEFAULT_NAME_1 = "DEFAULT_NAME_1";
    private final static String DEFAULT_PICTUREPATH_1 = "DEFAULT_PICTUREPATH_1";
    private final static String DEFAULT_EMAIL_1 = "DEFAULT_EMAIL_1";
    private final static String DEFAULT_ID_2 = "DEFAULT_ID_2";
    private final static String DEFAULT_NAME_2 = "DEFAULT_NAME_2";
    private final static String DEFAULT_PICTUREPATH_2 = "DEFAULT_PICTUREPATH_2";
    private final static String DEFAULT_EMAIL_2 = "DEFAULT_EMAIL_2";

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private final OAuthRepository oAuthRepository = Mockito.mock(OAuthRepository.class);
    private final AccessToken accessToken = Mockito.mock(AccessToken.class);
    private final AccessToken accessTokenBis = Mockito.mock(AccessToken.class);
    private final MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<User> userMutableLiveDataBis = new MutableLiveData<>();

    private OAuthViewModel viewModel;

    @Before
    public void setUp() {

        Mockito.doReturn(userMutableLiveData).when(oAuthRepository).getUserSignedInWithFacebook(accessToken);
        Mockito.doReturn(userMutableLiveDataBis).when(oAuthRepository).getUserSignedInWithFacebook(accessTokenBis);
        viewModel = new OAuthViewModel(oAuthRepository);
    }

    @Test
    public void checks_if_different_accessToken_gives_different_user_with_facebook() {
        // Given
        userMutableLiveData.setValue(getAnUser());
        userMutableLiveDataBis.setValue(getAnotherUser());

        // When
        User user = LiveDataTestUtils.getValueForTesting(viewModel.getFacebookUserInfo(accessToken));
        User userBis = LiveDataTestUtils.getValueForTesting(viewModel.getFacebookUserInfo(accessTokenBis));

        // Then
        assertEquals(getAnUser(), user);
        assertEquals(getAnotherUser(), userBis);
        assertNotEquals(user, userBis);

    }

    public User getAnUser(){
        return new User(
            DEFAULT_ID_1,
            DEFAULT_NAME_1,
            DEFAULT_PICTUREPATH_1,
            DEFAULT_EMAIL_1,
            null,
            null
        );
    }

    public User getAnotherUser(){
        return new User(
            DEFAULT_ID_2,
            DEFAULT_NAME_2,
            DEFAULT_PICTUREPATH_2,
            DEFAULT_EMAIL_2,
            null,
            null
        );
    }

}
