package com.davidvignon.go4lunch.ui.workmates;

import static org.junit.Assert.assertEquals;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.davidvignon.go4lunch.R;
import com.davidvignon.go4lunch.data.CurrentQueryRepository;
import com.davidvignon.go4lunch.data.workmate.Workmate;
import com.davidvignon.go4lunch.data.workmate.WorkmateRepository;
import com.davidvignon.go4lunch.utils.LiveDataTestUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class WorkmatesViewModelTest {

    private static final String DEFAULT_USER_UID = "DEFAULT_USER_UID";
    private static final String DEFAULT_USER_NAME = "DEFAULT_USER_NAME";
    private static final String DEFAULT_USER_EMAIL = "DEFAULT_USER_EMAIL";
    private static final String DEFAULT_USER_PICTURE_PATH = "DEFAULT_USER_PICTURE_PATH";
    private static final String DEFAULT_USER_SELECTED_RESTAURANT_ID = "DEFAULT_USER_SELECTED_RESTAURANT_ID";
    private static final String DEFAULT_USER_SELECTED_RESTAURANT_NAME = "DEFAULT_USER_SELECTED_RESTAURANT_NAME";


  private static final String DEFAULT_WORKMATE_ID = "DEFAULT_WORKMATE_ID";
  private static final String DEFAULT_WORKMATE_NAME = "DEFAULT_WORKMATE_NAME";
  private static final String DEFAULT_WORKMATE_EMAIL = "DEFAULT_WORKMATE_EMAIL";
  private static final String DEFAULT_WORKMATE_PICTURE_PATH = "DEFAULT_WORKMATE_PICTURE_PATH";
  private static final String DEFAULT_WORKMATE_SELECTED_RESTAURANT_ID = "DEFAULT_WORKMATE_SELECTED_RESTAURANT_ID";
  private static final String DEFAULT_WORKMATE_SELECTED_RESTAURANT_NAME = "DEFAULT_WORKMATE_SELECTED_RESTAURANT_NAME";

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private final Application application = Mockito.mock(Application.class);
    private final WorkmateRepository workmateRepository = Mockito.mock(WorkmateRepository.class);
    private final CurrentQueryRepository currentQueryRepository = Mockito.mock(CurrentQueryRepository.class);
    private final FirebaseAuth firebaseAuth = Mockito.mock(FirebaseAuth.class);
    private final FirebaseUser firebaseUser = Mockito.mock(FirebaseUser.class);

    private final MutableLiveData <List<Workmate>> workmateListMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData <String> currentRestaurantQueryLiveData = new MutableLiveData<>();

    private WorkmatesViewModel viewModel;

    @Before
    public void setUp(){
        Mockito.doReturn(" is").when(application).getString(R.string.a_restaurant_is_selected);
        Mockito.doReturn(" hasn't").when(application).getString(R.string.no_selected_restaurant);
        Mockito.doReturn(workmateListMutableLiveData).when(workmateRepository).getDataBaseUsersLiveData();
        Mockito.doReturn(currentRestaurantQueryLiveData).when(currentQueryRepository).getCurrentRestaurantQuery();
        workmateListMutableLiveData.setValue(getWorkmateList());

        Mockito.doReturn(DEFAULT_USER_NAME).when(firebaseUser).getDisplayName();
        Mockito.doReturn(DEFAULT_USER_UID).when(firebaseUser).getUid();
        Mockito.doReturn(DEFAULT_USER_EMAIL).when(firebaseUser).getEmail();
        Mockito.doReturn(firebaseUser).when(firebaseAuth).getCurrentUser();

        viewModel = new WorkmatesViewModel(application, workmateRepository, firebaseAuth, currentQueryRepository);
    }

    @Test
    public void initial_case(){
        // When
        List<WorkmatesViewState> viewStates = LiveDataTestUtils.getValueForTesting(viewModel.getWorkmatesViewStatesLiveData());

        // Then
        assertEquals(getDefaultWorkmateViewState(), viewStates);

    }

    @Test
    public void current_user_is_not_added_in_the_list(){
        // Given
        workmateListMutableLiveData.setValue(getWorkmateListWithCurrentUserInIt());

        // When
        List<WorkmatesViewState> viewStates = LiveDataTestUtils.getValueForTesting(viewModel.getWorkmatesViewStatesLiveData());

        // Then
        assertEquals(3, viewStates.size());
    }

    @Test
    public void if_query_only_matched_results_are_displayed(){
        // Given
        currentRestaurantQueryLiveData.setValue("d0");
        workmateListMutableLiveData.setValue(getWorkmateListWithRestaurantSelected());

        // When
        List<WorkmatesViewState> viewStates = LiveDataTestUtils.getValueForTesting(viewModel.getWorkmatesViewStatesLiveData());

        // Then
        assertEquals(1, viewStates.size());
    }

    @Test
    public void if_no_query_all_workmates_are_displayed(){
        // Given
        currentRestaurantQueryLiveData.setValue(null);

        // When
        List<WorkmatesViewState> viewStates = LiveDataTestUtils.getValueForTesting(viewModel.getWorkmatesViewStatesLiveData());

        // Then
        assertEquals(getDefaultWorkmateViewState(), viewStates);
    }

    @Test
    public void if_no_match_no_workmates_are_displayed(){
        // Given
        currentRestaurantQueryLiveData.setValue("query");

        // When
        List<WorkmatesViewState> viewStates = LiveDataTestUtils.getValueForTesting(viewModel.getWorkmatesViewStatesLiveData());

        // Then
        assertEquals(0, viewStates.size());
    }

    // region IN
    private List<Workmate> getWorkmateList(){
        List<Workmate> workmateList = new ArrayList<>();

        for(int i = 0; i < 3; i++){
            workmateList.add(new Workmate(
                DEFAULT_WORKMATE_ID + i,
                DEFAULT_WORKMATE_NAME + i,
                DEFAULT_WORKMATE_EMAIL + i,
                DEFAULT_WORKMATE_PICTURE_PATH + i,
                null,
                null
            ));
        }
        return workmateList;
    }

    private List<Workmate> getWorkmateListWithRestaurantSelected(){
        List<Workmate> workmateList = new ArrayList<>();

        for(int i = 0; i < 3; i++){
            workmateList.add(new Workmate(
                DEFAULT_WORKMATE_ID + i,
                DEFAULT_WORKMATE_NAME + i,
                DEFAULT_WORKMATE_EMAIL + i,
                DEFAULT_WORKMATE_PICTURE_PATH + i,
                 DEFAULT_WORKMATE_SELECTED_RESTAURANT_ID + i,
                DEFAULT_WORKMATE_SELECTED_RESTAURANT_NAME + i
            ));
        }
        return workmateList;
    }

    private List<Workmate> getWorkmateListWithCurrentUserInIt(){
        List<Workmate> workmateList = new ArrayList<>();

        workmateList.add(new Workmate(
            DEFAULT_USER_UID ,
            DEFAULT_USER_NAME,
            DEFAULT_USER_EMAIL,
            DEFAULT_USER_PICTURE_PATH,
            DEFAULT_USER_SELECTED_RESTAURANT_ID,
            DEFAULT_USER_SELECTED_RESTAURANT_NAME
        ));

        for(int i = 0; i < 3; i++){
            workmateList.add(new Workmate(
                DEFAULT_WORKMATE_ID + i,
                DEFAULT_WORKMATE_NAME + i,
                DEFAULT_WORKMATE_EMAIL + i,
                DEFAULT_WORKMATE_PICTURE_PATH + i,
                DEFAULT_WORKMATE_SELECTED_RESTAURANT_ID + i,
                DEFAULT_WORKMATE_SELECTED_RESTAURANT_NAME + i
            ));
        }
        return workmateList;
    }
    // endregion IN

    // region OUT
    private List<WorkmatesViewState> getDefaultWorkmateViewState(){
        List<WorkmatesViewState> workmateList = new ArrayList<>();

        for(int i = 0; i < 3; i++){
            workmateList.add(new WorkmatesViewState(
                DEFAULT_WORKMATE_ID + i,
                DEFAULT_WORKMATE_NAME + i + " hasn't",
                DEFAULT_WORKMATE_PICTURE_PATH + i,
                null,
                null
            ));
        }
        return workmateList;
    }
    // endregion OUT

}
