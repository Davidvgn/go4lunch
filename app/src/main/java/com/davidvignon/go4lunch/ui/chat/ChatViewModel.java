package com.davidvignon.go4lunch.ui.chat;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.davidvignon.go4lunch.R;
import com.davidvignon.go4lunch.data.chat_message.ChatMessage;
import com.davidvignon.go4lunch.data.chat_message.ChatMessageRepository;
import com.davidvignon.go4lunch.data.workmate.Workmate;
import com.davidvignon.go4lunch.data.workmate.WorkmateRepository;
import com.davidvignon.go4lunch.ui.utils.SingleLiveEvent;
import com.google.firebase.auth.FirebaseAuth;

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

    private final Application application;

    private final ChatMessageRepository chatMessageRepository;
    private final FirebaseAuth firebaseAuth;
    private final DateTimeFormatter timeFormatter;

    private final MediatorLiveData<ChatViewState> chatViewStateMediatorLiveData = new MediatorLiveData<>();
    private final SingleLiveEvent<String> showToastSingleLiveEvent = new SingleLiveEvent<>();
    private final MutableLiveData<Boolean> isMessageSentMutableLiveData = new MutableLiveData<>();

    private final String workmateId;

    @Inject
    public ChatViewModel(
        Application application,
        @NonNull WorkmateRepository workmateRepository,
        @NonNull ChatMessageRepository chatMessageRepository,
        @NonNull SavedStateHandle savedStateHandle,
        FirebaseAuth firebaseAuth
    ) {
        this.application = application;
        this.chatMessageRepository = chatMessageRepository;
        this.firebaseAuth = firebaseAuth;

        timeFormatter = DateTimeFormatter.ofPattern(application.getString(R.string.date_time_formater));

        workmateId = savedStateHandle.get(KEY_WORKMATE_ID);
        LiveData<Workmate> workmateLiveData = workmateRepository.getWorkmateInfoLiveData(workmateId);
        LiveData<List<ChatMessage>> chatMessagesLiveData = chatMessageRepository.getChatMessagesLiveData(workmateId);

        isMessageSentMutableLiveData.setValue(false);
        chatViewStateMediatorLiveData.addSource(workmateLiveData, workmate -> ChatViewModel.this.combine(workmate, chatMessagesLiveData.getValue()));

        chatViewStateMediatorLiveData.addSource(chatMessagesLiveData, sentMessagesList -> ChatViewModel.this.combine(workmateLiveData.getValue(), sentMessagesList));
    }

    public void combine(@Nullable Workmate workmate, @Nullable List<ChatMessage> chatMessages) {
        if (workmate == null || chatMessages == null) {
            return;
        }

        List<ChatViewStateItem> chatViewStateItems = new ArrayList<>();

        for (ChatMessage chatMessage : chatMessages) {
            boolean isFromWorkmate = chatMessage.getSenderId().equals(workmateId);

            if (firebaseAuth.getCurrentUser() != null &&
                firebaseAuth.getCurrentUser().getDisplayName() != null) {

                chatViewStateItems.add(
                    new ChatViewStateItem(
                        chatMessage.getUuid(),
                        isFromWorkmate ? workmate.getName() : firebaseAuth.getCurrentUser().getDisplayName(),
                        chatMessage.getMessage(),
                        Instant.ofEpochMilli(chatMessage.getEpochMilli()).atZone(ZoneId.systemDefault()).format(timeFormatter),
                        !isFromWorkmate
                    )
                );
            }
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

    public SingleLiveEvent<String> getShowToastSingleLiveEvent() {
        return showToastSingleLiveEvent;
    }

    public void sendMessage(String message) {
        if (!message.isEmpty()) {
            chatMessageRepository.sendMessage(message, workmateId);
            isMessageSentMutableLiveData.setValue(true);
        } else {
            showToastSingleLiveEvent.setValue(application.getString(R.string.message_is_empty));
        }
    }

    public LiveData<Boolean> getIsMessageSentValueLiveData() {
        return isMessageSentMutableLiveData;
    }

}
