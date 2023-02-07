package com.davidvignon.go4lunch.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.davidvignon.go4lunch.R;
import com.davidvignon.go4lunch.data.google_places.LocationRepository;
import com.davidvignon.go4lunch.data.permission.PermissionRepository;
import com.davidvignon.go4lunch.data.users.UserRepository;
import com.davidvignon.go4lunch.ui.utils.EventWrapper;
import com.davidvignon.go4lunch.ui.utils.SingleLiveEvent;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

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
    private final MutableLiveData<String> picturePathMutableLivaData = new MutableLiveData<>();
    private final MutableLiveData<List<String>> nameAndEmailMutableLiveData = new MutableLiveData<>();

    private final SingleLiveEvent<Void> notificationDialogSingleliveEvent = new SingleLiveEvent<>();

    private final MediatorLiveData<EventWrapper<MainAction>> mainActionsMediatorLiveData = new MediatorLiveData<>();

    @Inject
    public MainViewModel(@ApplicationContext Context context,
        @NonNull UserRepository userRepository,
        @NonNull PermissionRepository permissionRepository,
        @NonNull LocationRepository locationRepository
    ) {
        this.context = context;
        this.permissionRepository = permissionRepository;
        this.locationRepository = locationRepository;

        LiveData<String> placeIdLiveData = userRepository.getRestaurantPlaceId();
        LiveData<DocumentSnapshot> documentSnapshotLiveData = userRepository.getUserPicturePathLiveData();
        LiveData<List<String>> userNameAndEmailLiveData = userRepository.getUserNameAndEmailLiveData();

        mainActionsMediatorLiveData.addSource(userNameAndEmailLiveData, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                combineEventAndUserInformations(strings, documentSnapshotLiveData.getValue(), placeIdLiveData.getValue(), onMyLunchClickedMutableLiveData.getValue());
            }
        });

        mainActionsMediatorLiveData.addSource(documentSnapshotLiveData, new Observer<DocumentSnapshot>() {
            @Override
            public void onChanged(DocumentSnapshot documentSnapshot) {
                combineEventAndUserInformations(nameAndEmailMutableLiveData.getValue(), documentSnapshot, placeIdLiveData.getValue(), onMyLunchClickedMutableLiveData.getValue());
            }
        });

        mainActionsMediatorLiveData.addSource(placeIdLiveData, new Observer<String>() {
            @Override
            public void onChanged(String placeId) {
                combineEventAndUserInformations(nameAndEmailMutableLiveData.getValue(), documentSnapshotLiveData.getValue(), placeId, onMyLunchClickedMutableLiveData.getValue());
            }
        });

        mainActionsMediatorLiveData.addSource(onMyLunchClickedMutableLiveData, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean onMyLunchClicked) {
                combineEventAndUserInformations(nameAndEmailMutableLiveData.getValue(), documentSnapshotLiveData.getValue(), placeIdLiveData.getValue(), onMyLunchClicked);
            }
        });
    }

    private void combineEventAndUserInformations(List<String> nameAndEmail, DocumentSnapshot document, @Nullable String placeId, @Nullable Boolean onMyLunchClicked) {

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context.getApplicationContext());
        if (!notificationManager.areNotificationsEnabled()) {
            notificationDialogSingleliveEvent.call();
        }

        if (document != null) {
            String userPicturePath = document.get("picturePath").toString();
            picturePathMutableLivaData.setValue(userPicturePath);
        }

        if (nameAndEmail != null) {
            nameAndEmailMutableLiveData.setValue(nameAndEmail);
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

    public LiveData<EventWrapper<MainAction>> getMainActionLiveData() {
        return mainActionsMediatorLiveData;
    }

    public LiveData<String> getPicturePathLiveData() {
        return picturePathMutableLivaData;
    }

    public LiveData<List<String>> getNameAndEmailLiveData() {
        return nameAndEmailMutableLiveData;
    }

    public void onMyLunchClicked() {
        onMyLunchClickedMutableLiveData.setValue(true);
    }

    public SingleLiveEvent<Void> getNotificationDialogSingleLiveEvent() {
        return notificationDialogSingleliveEvent;
    }

}
