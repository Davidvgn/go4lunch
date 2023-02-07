//package com.davidvignon.go4lunch.ui.main;
//
//import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
//
//import com.davidvignon.go4lunch.data.google_places.LocationRepository;
//import com.davidvignon.go4lunch.data.permission.PermissionRepository;
//import com.davidvignon.go4lunch.data.users.UserRepository;
//
//import org.junit.Before;
//import org.junit.Rule;
//import org.mockito.Mockito;
//
//public class MainViewModelTest {
//
//    @Rule
//    private InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
//
//    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
//    private final PermissionRepository permissionRepository = Mockito.mock(PermissionRepository.class);
//    private final LocationRepository locationRepository = Mockito.mock(LocationRepository.class);
//
//    private MainViewModel viewModel;
//
//    @Before
//    public void setUp(){
//
//        Mockito.doReturn().when(userRepository).getRestaurantPlaceId();
//        Mockito.doReturn().when(permissionRepository).isLocationPermissionGranted();
//        Mockito.doReturn().when(locationRepository).startLocationRequest();
//        Mockito.doReturn().when(locationRepository).stopLocationRequest();
//
//    }
//}
