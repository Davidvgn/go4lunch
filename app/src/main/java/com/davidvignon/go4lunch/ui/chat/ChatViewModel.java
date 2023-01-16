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
import com.davidvignon.go4lunch.data.chat.ChatMessage;
import com.davidvignon.go4lunch.data.chat.ChatMessageRepository;
import com.davidvignon.go4lunch.data.workmate.Workmate;
import com.davidvignon.go4lunch.data.workmate.WorkmateRepository;

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

    //TODO DAVID : ordre des messages / le destinataire n'as pas le message qui apparait
    WorkmateRepository workmateRepository;
    ChatMessageRepository chatMessageRepository;

    private final MediatorLiveData<Chat> mediatorLiveData = new MediatorLiveData<>();
    private final MutableLiveData<String> workmateIdMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<ChatMessageViewState>> chatMessageMutableLiveData = new MutableLiveData<>();

    @Inject
    public ChatViewModel(@NonNull WorkmateRepository workmateRepository, @NonNull ChatMessageRepository chatMessageRepository, @NonNull SavedStateHandle savedStateHandle
    ) {
        this.workmateRepository = workmateRepository;
        this.chatMessageRepository = chatMessageRepository;

        String workmateId = savedStateHandle.get(KEY_WORKMATE_ID);
        LiveData<Workmate> workmateLiveData = workmateRepository.getWorkmateInfo(workmateId);
        LiveData<List<ChatMessage>> chatMessageLiveData = chatMessageRepository.getMessagetest(workmateId);
        workmateIdMutableLiveData.setValue(workmateId);

        mediatorLiveData.addSource(chatMessageLiveData, new Observer<List<ChatMessage>>() {
            @Override
            public void onChanged(List<ChatMessage> chatMessages) {
                combine(workmateLiveData.getValue(), chatMessages);
            }
        });

        mediatorLiveData.addSource(workmateLiveData, new Observer<Workmate>() {
            @Override
            public void onChanged(Workmate workmate) {
                combine(workmate, chatMessageLiveData.getValue());
            }
        });
    }


    public void combine(Workmate workmate, List<ChatMessage> chatMessageList) {
        List<ChatMessageViewState> viewStates = new ArrayList<>();
        Chat chat;

        if (chatMessageList != null) {

            String formattedTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
            String formattedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            for (ChatMessage chatMessage : chatMessageList) {
                viewStates.add(new ChatMessageViewState(
                    "",
                    "",
                    "", //todo david : afficher celui qui envoie le message pas le workmate
                    chatMessage.getMessage(),
                    "le " + formattedDate + " à " + formattedTime));//todo david : pas bon la date est MAJ pour tous les messages à l'heure du dernier message envoyé

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
        chatMessageRepository.sendMessage(message, workmateIdMutableLiveData.getValue());
    }
}
