package com.davidvignon.go4lunch.data.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ChatMessageRepository {

    @NonNull
    private final FirebaseFirestore firebaseFirestore;

    @NonNull
    private final FirebaseAuth firebaseAuth;

    @Inject
    public ChatMessageRepository(@NonNull FirebaseAuth firebaseAuth, @NonNull FirebaseFirestore firebaseFirestore) {
        this.firebaseAuth = firebaseAuth;
        this.firebaseFirestore = firebaseFirestore;
    }

    public void sendMessage(String message, String userReceiverId) {
        firebaseFirestore
            .collection("chat")
            .document("conversations")
            .collection("allConversations")
            .add(new ChatMessage(
                firebaseAuth.getCurrentUser().getUid(),
                userReceiverId,
                message,
                "now"));//todo david
    }

    public LiveData<List<ChatMessage>> getSentMessagesLiveData(String receiver, String sender) {
        MutableLiveData<List<ChatMessage>> chatMessageMutableLiveData = new MutableLiveData<>();
        firebaseFirestore.collection("chat")
            .document("conversations")
            .collection("allConversations")
            .whereEqualTo("receiver", receiver)
            .whereEqualTo("sender", sender)
            .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null)
                    chatMessageMutableLiveData.setValue(value.toObjects(ChatMessage.class));
            }
        });
        return chatMessageMutableLiveData;
    }

    public LiveData<List<ChatMessage>> getReceivedMessagesLiveData(String receiver, String sender) {
        MutableLiveData<List<ChatMessage>> chatMessageMutableLiveData = new MutableLiveData<>();
        firebaseFirestore.collection("chat")
            .document("conversations")
            .collection("allConversations")
            .whereEqualTo("receiver", sender)
            .whereEqualTo("sender", receiver)
            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (value != null)
                        chatMessageMutableLiveData.setValue(value.toObjects(ChatMessage.class));
                }
            });
        return chatMessageMutableLiveData;
    }
}
