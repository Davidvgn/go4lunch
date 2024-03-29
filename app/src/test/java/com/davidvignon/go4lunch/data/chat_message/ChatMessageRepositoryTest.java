package com.davidvignon.go4lunch.data.chat_message;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

public class ChatMessageRepositoryTest {

    private static final String DEFAULT_USER_ID = "DEFAULT_USER_ID";
    private static final String DEFAULT_USER_NAME = "DEFAULT_USER_NAME";
    private static final String DEFAULT_USER_EMAIL = "DEFAULT_USER_EMAIL";

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private final FirebaseAuth firebaseAuth = Mockito.mock(FirebaseAuth.class);
    private final FirebaseUser firebaseUser = Mockito.mock(FirebaseUser.class);
    private final FirebaseFirestore firebaseFirestore = Mockito.mock(FirebaseFirestore.class);


    private ChatMessageRepository chatMessageRepository;


    @Before
    public void setUp() {

        Mockito.doReturn(DEFAULT_USER_ID).when(firebaseUser).getUid();
        Mockito.doReturn(DEFAULT_USER_NAME).when(firebaseUser).getDisplayName();
        Mockito.doReturn(DEFAULT_USER_EMAIL).when(firebaseUser).getEmail();
        Mockito.doReturn(firebaseUser).when(firebaseAuth).getCurrentUser();

        chatMessageRepository = new ChatMessageRepository(firebaseAuth, firebaseFirestore);

    }

    @Test
    public void send_messages_works() {
        // Given
        CollectionReference chatCollectionReference = Mockito.mock(CollectionReference.class);
        DocumentReference roomDocumentReference = Mockito.mock(DocumentReference.class);
        CollectionReference messagesCollectionReference = Mockito.mock(CollectionReference.class);
        Task addTask = Mockito.mock(Task.class);
        Mockito.doReturn(chatCollectionReference).when(firebaseFirestore).collection("chat");
        Mockito.doReturn(roomDocumentReference).when(chatCollectionReference).document(anyString());
        Mockito.doReturn(messagesCollectionReference).when(roomDocumentReference).collection("messages");
        Mockito.doReturn(addTask).when(messagesCollectionReference).add(any());

        // When
        chatMessageRepository.sendMessage("test", "userReceiverId");

        // Then
        Mockito.verify(firebaseFirestore).collection("chat");
        Mockito.verify(chatCollectionReference).document(anyString());
        Mockito.verify(roomDocumentReference).collection("messages");
        Mockito.verify(messagesCollectionReference).add(any());
    }
}


