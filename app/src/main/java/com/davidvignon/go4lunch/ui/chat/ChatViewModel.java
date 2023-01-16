package com.davidvignon.go4lunch.ui.chat;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.davidvignon.go4lunch.data.Chat;
import com.davidvignon.go4lunch.data.chatMessage.ChatMessage;
import com.davidvignon.go4lunch.data.chatMessage.ChatMessageRepository;
import com.davidvignon.go4lunch.data.workmate.Workmate;
import com.davidvignon.go4lunch.data.workmate.WorkmateRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.time.LocalDate;
import java.time.LocalTime;
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

    private final MediatorLiveData<Chat> mediatorLiveData = new MediatorLiveData<>();
    private final MutableLiveData<String> workmateIdMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<ChatMessageViewState>> chatMessageMutableLiveData = new MutableLiveData<>();

    @Inject
    public ChatViewModel(@NonNull FirebaseUser firebaseUser, @NonNull WorkmateRepository workmateRepository, @NonNull ChatMessageRepository chatMessageRepository, @NonNull SavedStateHandle savedStateHandle
    ) {
        this.chatMessageRepository = chatMessageRepository;
        this.firebaseUser = firebaseUser;


        String workmateId = savedStateHandle.get(KEY_WORKMATE_ID);
        LiveData<Workmate> workmateLiveData = workmateRepository.getWorkmateInfo(workmateId);
        LiveData<List<ChatMessage>> chatMessageSendLiveData = chatMessageRepository.getSentMessagesLiveData(workmateId, firebaseUser.getUid());
        LiveData<List<ChatMessage>> chatMessageReceivedLiveData = chatMessageRepository.getReceivedMessagesLiveData(workmateId, firebaseUser.getUid());
        workmateIdMutableLiveData.setValue(workmateId);


        mediatorLiveData.addSource(chatMessageReceivedLiveData, new Observer<List<ChatMessage>>() {
            @Override
            public void onChanged(List<ChatMessage> receivedMessagesList) {
                combine(workmateLiveData.getValue(), chatMessageSendLiveData.getValue(), receivedMessagesList);
            }
        });

        mediatorLiveData.addSource(chatMessageSendLiveData, new Observer<List<ChatMessage>>() {
            @Override
            public void onChanged(List<ChatMessage> sentMessagesList) {
                combine(workmateLiveData.getValue(), sentMessagesList, chatMessageReceivedLiveData.getValue());
            }
        });

        mediatorLiveData.addSource(workmateLiveData, new Observer<Workmate>() {
            @Override
            public void onChanged(Workmate workmate) {
                combine(workmate, chatMessageSendLiveData.getValue(), chatMessageReceivedLiveData.getValue());
            }
        });
    }


    public void combine(Workmate workmate, List<ChatMessage> sentMessagesList, List<ChatMessage> receivedMessagesList) {
        List<ChatMessageViewState> viewStates = new ArrayList<>();
        Chat chat;
        String formattedTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        String formattedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        if (sentMessagesList != null || receivedMessagesList != null) {

            if (sentMessagesList != null) {
                for (ChatMessage chatMessage : sentMessagesList) {
                    viewStates.add(new ChatMessageViewState(
                        "",
                        firebaseUser.getDisplayName(),
                        chatMessage.getMessage(),
                        "le " + formattedDate + " à " + formattedTime));//todo david : pas bon la date est MAJ pour tous les messages à l'heure du dernier message envoyé

                }
            }

            if (receivedMessagesList != null) {
                for (ChatMessage chatMessage : receivedMessagesList) {
                    viewStates.add(new ChatMessageViewState(
                        "",
                        workmate.getName(),
                        chatMessage.getMessage(),
                        "le " + formattedDate + " à " + formattedTime));//todo david : pas bon la date est MAJ pour tous les messages à l'heure du dernier message envoyé

                }
            }
            chatMessageMutableLiveData.setValue(viewStates);
        }


        chat = new Chat(
            workmate.getName(),
            workmate.getPicturePath()
        );

        mediatorLiveData.setValue(chat);
    }

    @NonNull
    public LiveData<Chat> getChat() {
        return mediatorLiveData;
    }

    public LiveData<List<ChatMessageViewState>> getChatmess() {
        return chatMessageMutableLiveData;
    }

    public void sendMessage(String message) {
        if (message != "") {
            chatMessageRepository.sendMessage(message, workmateIdMutableLiveData.getValue());
        } //todo david faire un TOAST le cas échéant
    }

}
