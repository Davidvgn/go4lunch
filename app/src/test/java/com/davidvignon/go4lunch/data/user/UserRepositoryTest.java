package com.davidvignon.go4lunch.data.user;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;

import com.davidvignon.go4lunch.data.users.User;
import com.davidvignon.go4lunch.data.users.UserRepository;
import com.davidvignon.go4lunch.utils.LiveDataTestUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;

public class UserRepositoryTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private final FirebaseFirestore firebaseFirestore = Mockito.mock(FirebaseFirestore.class);
    private final CollectionReference collectionReference = Mockito.mock(CollectionReference.class);
    private final Task task = Mockito.mock(Task.class);
    private final QuerySnapshot querySnapshot = Mockito.mock(QuerySnapshot.class);
    private final List users = Mockito.mock(List.class);

    private UserRepository firestoreRepository;

    @Before
    public void setUp() {
        Mockito.doReturn(collectionReference).when(firebaseFirestore).collection("users");
        Mockito.doReturn(task).when(collectionReference).get();
        Mockito.doNothing().when(task).addOnCompleteListener(any());

        firestoreRepository = new UserRepository(firebaseFirestore);
    }

    @Test
    public void nominal_case() {
        // Given
        ArgumentCaptor<OnCompleteListener> onCompleteListenerArgumentCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);
        Mockito.doReturn(true).when(task).isSuccessful();
        Mockito.doReturn(querySnapshot).when(task).getResult();
        Mockito.doReturn(users).when(querySnapshot).toObjects(User.class);

        LiveData<List<User>> liveData = firestoreRepository.getDataBaseUsers();
        Mockito.verify(task).addOnCompleteListener(onCompleteListenerArgumentCaptor.capture());

        onCompleteListenerArgumentCaptor.getValue().onComplete(task);

        List<User> response = LiveDataTestUtils.getValueForTesting(liveData);

        assertEquals(response, users);

    }

}
