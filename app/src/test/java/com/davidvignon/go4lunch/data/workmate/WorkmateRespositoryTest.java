package com.davidvignon.go4lunch.data.workmate;

public class WorkmateRespositoryTest {
}


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