package com.davidvignon.go4lunch.data.firestore;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;

import com.davidvignon.go4lunch.data.FirestoreRepository;
import com.davidvignon.go4lunch.utils.LiveDataTestUtils;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

public class FirestoreRepositoryTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private final FirebaseFirestore firebaseFirestore = Mockito.mock(FirebaseFirestore.class);

    private FirestoreRepository firestoreRepository;

    @Before
    public void setUp(){
        firestoreRepository = new FirestoreRepository(firebaseFirestore);
    }

    @Test
    public void nominal_case(){
        LiveData<QuerySnapshot> liveData = firestoreRepository.getDataBaseUsers();

        QuerySnapshot response = LiveDataTestUtils.getValueForTesting(liveData);

    }

}
