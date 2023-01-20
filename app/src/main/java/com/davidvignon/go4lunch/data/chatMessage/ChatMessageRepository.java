package com.davidvignon.go4lunch.data.chatMessage;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

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
        final String roomId = getRoomId(userReceiverId);

        ChatMessage chatMessage = new ChatMessage(
            UUID.randomUUID().toString(),
            firebaseAuth.getCurrentUser().getUid(),
            userReceiverId,
            message,
            Instant.now().toEpochMilli()
        );

        firebaseFirestore
            .collection("chat")
            .document(roomId)
            .collection("messages")
            .add(chatMessage);
    }

    public LiveData<List<ChatMessage>> getChatMessagesLiveData(String userReceiverId) {
        final String roomId = getRoomId(userReceiverId);

        MutableLiveData<List<ChatMessage>> chatMessageMutableLiveData = new MutableLiveData<>();
        firebaseFirestore
            .collection("chat")
            .document(roomId)
            .collection("messages")
            .orderBy("epochMilli", Query.Direction.DESCENDING)
            .addSnapshotListener((value, error) -> {
                if (value != null) {
                    chatMessageMutableLiveData.setValue(value.toObjects(ChatMessage.class));
                }
            });
        return chatMessageMutableLiveData;
    }

    @NonNull
    private String getRoomId(String userReceiverId) {
        String myId = firebaseAuth.getCurrentUser().getUid();

        final String roomId;

        if (myId.compareTo(userReceiverId) > 0) {
            roomId = userReceiverId + "_" + myId;
        } else {
            roomId = myId + "_" + userReceiverId;
        }
        return roomId;
    }
}
