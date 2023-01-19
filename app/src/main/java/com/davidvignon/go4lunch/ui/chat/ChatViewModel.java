package com.davidvignon.go4lunch.ui.chat;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.davidvignon.go4lunch.data.chatMessage.ChatMessage;
import com.davidvignon.go4lunch.data.chatMessage.ChatMessageRepository;
import com.davidvignon.go4lunch.data.workmate.Workmate;
import com.davidvignon.go4lunch.data.workmate.WorkmateRepository;
import com.google.firebase.auth.FirebaseUser;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ChatViewModel extends ViewModel {

    private static final String KEY_WORKMATE_ID = "KEY_WORKMATE_ID";

    public static Intent navigate(Context context, String workmateId) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(KEY_WORKMATE_ID, workmateId);
        return intent;
    }

    //TODO DAVID : ordre des messages + date
    private final ChatMessageRepository chatMessageRepository;
    private final FirebaseUser firebaseUser;

    private final MediatorLiveData<ChatViewState> chatViewStateMediatorLiveData = new MediatorLiveData<>();

    private final String workmateId;

    @Inject
    public ChatViewModel(@NonNull FirebaseUser firebaseUser, @NonNull WorkmateRepository workmateRepository, @NonNull ChatMessageRepository chatMessageRepository, @NonNull SavedStateHandle savedStateHandle
    ) {
        this.chatMessageRepository = chatMessageRepository;
        this.firebaseUser = firebaseUser;

        workmateId = savedStateHandle.get(KEY_WORKMATE_ID);
        LiveData<Workmate> workmateLiveData = workmateRepository.getWorkmateInfo(workmateId);
        LiveData<List<ChatMessage>> chatMessagesLiveData = chatMessageRepository.getChatMessagesLiveData(workmateId);

        chatViewStateMediatorLiveData.addSource(workmateLiveData, new Observer<Workmate>() {
            @Override
            public void onChanged(Workmate workmate) {
                combine(workmate, chatMessagesLiveData.getValue());
            }
        });

        chatViewStateMediatorLiveData.addSource(chatMessagesLiveData, new Observer<List<ChatMessage>>() {
            @Override
            public void onChanged(List<ChatMessage> sentMessagesList) {
                combine(workmateLiveData.getValue(), sentMessagesList);
            }
        });
    }

    public void combine(@Nullable Workmate workmate, @Nullable List<ChatMessage> chatMessages) {
        if (workmate == null || chatMessages == null) {
            return;
        }

        List<ChatViewStateItem> chatViewStateItems = new ArrayList<>();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("'le' dd/MM/yyyy à HH:mm");

        for (ChatMessage chatMessage : chatMessages) {
            boolean isFromWorkmate = chatMessage.getSenderId().equals(workmateId);

            chatViewStateItems.add(
                new ChatViewStateItem(
                    chatMessage.getUuid(),
                    isFromWorkmate ? workmate.getName() : firebaseUser.getDisplayName(),
                    chatMessage.getMessage(),
                    Instant.ofEpochMilli(chatMessage.getEpochMilli()).atZone(ZoneId.systemDefault()).format(timeFormatter)
                )
            );
        }

        chatViewStateMediatorLiveData.setValue(
            new ChatViewState(
                workmate.getName(),
                workmate.getPicturePath(),
                chatViewStateItems
            )
        );
    }

    @NonNull
    public LiveData<ChatViewState> getChatViewStateLiveData() {
        return chatViewStateMediatorLiveData;
    }

    public void sendMessage(String message) {
        if (!message.equals("")) {
            chatMessageRepository.sendMessage(message, workmateId);
        } //todo david faire un TOAST le cas échéant
    }

}
