package com.davidvignon.go4lunch.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.arch.core.util.Function;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.davidvignon.go4lunch.R;
import com.davidvignon.go4lunch.data.google_places.LocationRepository;
import com.davidvignon.go4lunch.data.permission.PermissionRepository;
import com.davidvignon.go4lunch.data.users.User;
import com.davidvignon.go4lunch.data.users.UserRepository;
import com.davidvignon.go4lunch.ui.utils.EventWrapper;
import com.davidvignon.go4lunch.ui.utils.SingleLiveEvent;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;

@HiltViewModel
public class MainViewModel extends ViewModel {


    private Context context;
    @NonNull
    private final PermissionRepository permissionRepository;
    @NonNull
    private final LocationRepository locationRepository;

    private final MutableLiveData<Boolean> onMyLunchClickedMutableLiveData = new MutableLiveData<>();
    private final LiveData<MainViewState> mainViewStateLiveData;

    private final SingleLiveEvent<Void> notificationDialogSingleLiveEvent = new SingleLiveEvent<>();

    private final MediatorLiveData<EventWrapper<MainAction>> mainActionsMediatorLiveData = new MediatorLiveData<>();

    @Inject
    public MainViewModel(
        @ApplicationContext Context context,
        @NonNull UserRepository userRepository,
        @NonNull PermissionRepository permissionRepository,
        @NonNull LocationRepository locationRepository
    ) {
        this.context = context;
        this.permissionRepository = permissionRepository;
        this.locationRepository = locationRepository;

        LiveData<String> placeIdLiveData = userRepository.getRestaurantPlaceId();
        LiveData<User> userLiveData = userRepository.getUserLiveData();

        mainViewStateLiveData = Transformations.map(userLiveData, new Function<User, MainViewState>() {
            @Override
            public MainViewState apply(User user) {
                return new MainViewState(
                    user.getName(),
                    user.getEmail(),
                    user.getPicturePath()
                );
            }
        });

        mainActionsMediatorLiveData.addSource(placeIdLiveData, new Observer<String>() {
            @Override
            public void onChanged(String placeId) {
                combineEvent(placeId, onMyLunchClickedMutableLiveData.getValue());
            }
        });

        mainActionsMediatorLiveData.addSource(onMyLunchClickedMutableLiveData, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean onMyLunchClicked) {
                combineEvent(placeIdLiveData.getValue(), onMyLunchClicked);
            }
        });
    }

    private void combineEvent(@Nullable String placeId, @Nullable Boolean onMyLunchClicked) {

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context.getApplicationContext());
        if (!notificationManager.areNotificationsEnabled()) {
            notificationDialogSingleLiveEvent.call();
        }

        if (onMyLunchClicked != null && onMyLunchClicked) {
            onMyLunchClickedMutableLiveData.setValue(false);
            if (placeId == null) {
                mainActionsMediatorLiveData.setValue(new EventWrapper<>(new MainAction.Toast(R.string.no_restaurant_selected)));
            } else {
                mainActionsMediatorLiveData.setValue(new EventWrapper<>(new MainAction.DetailNavigation(placeId)));
            }
        }
    }

    @SuppressLint("MissingPermission")
    public void refresh() {
        if (permissionRepository.isLocationPermissionGranted()) {
            locationRepository.startLocationRequest();
        } else {
            locationRepository.stopLocationRequest();
        }
    }

    public LiveData<MainViewState> getMainViewStateLiveData() {
        return mainViewStateLiveData;
    }

    public LiveData<EventWrapper<MainAction>> getMainActionLiveData() {
        return mainActionsMediatorLiveData;
    }

    public void onMyLunchClicked() {
        onMyLunchClickedMutableLiveData.setValue(true);
    }

    public SingleLiveEvent<Void> getNotificationDialogSingleLiveEvent() {
        return notificationDialogSingleLiveEvent;
    }

}
