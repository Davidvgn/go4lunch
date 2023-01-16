package com.davidvignon.go4lunch.data.chat;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.davidvignon.go4lunch.data.Chat;
import com.davidvignon.go4lunch.data.workmate.Workmate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ChatMessageRepository {

    @NonNull
    private final FirebaseFirestore firebaseFirestore;

    @Inject
    public ChatMessageRepository(@NonNull FirebaseFirestore firebaseFirestore) {
        this.firebaseFirestore = firebaseFirestore;
    }



    public void sendMessage(String message,String userReceiverId) {
        firebaseFirestore
            .collection("chat")
            .document("conversations")
            .collection("allConversations")
            .document()
            .set(new ChatMessage(
                userReceiverId,
                message,
                "now"

            ));
    }


    public LiveData<String> getMessagesLiveData(String sender, String receiver) {
        MutableLiveData<List<ChatMessage>> chatMessageMutableLiveData = new MutableLiveData<>();

        firebaseFirestore.collection("chat")
            .whereEqualTo("sender", sender)
            .whereEqualTo("receiver", receiver)
            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                }
            });

        return null;
    }

    public LiveData<List<ChatMessage>> getMessagetest(String receiver) {
        MutableLiveData<List<ChatMessage>> chatMessageMutableLiveData = new MutableLiveData<>();
        firebaseFirestore.collection("chat")
            .document("conversations")
            .collection("allConversations")
            .whereEqualTo("recipient", receiver)
            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (value != null) {
                        chatMessageMutableLiveData.setValue(value.toObjects(ChatMessage.class));

                    }
                }
            });
        return chatMessageMutableLiveData;
    }


}
