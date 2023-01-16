//package com.davidvignon.go4lunch.ui.workmates;
//
//import static org.junit.Assert.assertEquals;
//
//import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
//import androidx.lifecycle.LiveData;
//
//import com.davidvignon.go4lunch.data.Workmates;
//import com.davidvignon.go4lunch.data.workmate.WorkmatesRepository;
//import com.davidvignon.go4lunch.data.users.User;
//import com.davidvignon.go4lunch.utils.LiveDataTestUtils;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.firestore.CollectionReference;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QuerySnapshot;
//
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Mockito;
//
//import java.util.List;
//
//@SuppressWarnings({"rawtypes", "unchecked"})
//public class WorkmatesViewModelTest {
//
//    @Rule
//    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
//
//    private final FirebaseFirestore firebaseFirestore = Mockito.mock(FirebaseFirestore.class);
//    private final CollectionReference collectionReference = Mockito.mock(CollectionReference.class);
//    private final Task task = Mockito.mock(Task.class);
//    private final QuerySnapshot querySnapshot = Mockito.mock(QuerySnapshot.class);
//    private final List users = Mockito.mock(List.class);
//
//    private WorkmatesRepository workmatesRepository;
//
//    @Before
//    public void setUp() {
//        Mockito.doReturn(collectionReference).when(firebaseFirestore).collection("users");
//        Mockito.doReturn(task).when(collectionReference).get();
//
//        workmatesRepository = new WorkmatesRepository(firebaseFirestore);
//    }
//
//    @Test
//    public void nominal_case() {
//        // Given
//        ArgumentCaptor<OnCompleteListener> onCompleteListenerArgumentCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);
//        Mockito.doReturn(true).when(task).isSuccessful();
//        Mockito.doReturn(querySnapshot).when(task).getResult();
//        Mockito.doReturn(users).when(querySnapshot).toObjects(User.class);
//
//        // When
//        LiveData<List<Workmates>> liveData = workmatesRepository.getDataBaseUsers();
//        Mockito.verify(task).addOnCompleteListener(onCompleteListenerArgumentCaptor.capture());
//
//        onCompleteListenerArgumentCaptor.getValue().onComplete(task);
//
//        List<Workmates> response = LiveDataTestUtils.getValueForTesting(liveData);
//
//        // Then
//        assertEquals(response, users);
//
//    }
//
//}
