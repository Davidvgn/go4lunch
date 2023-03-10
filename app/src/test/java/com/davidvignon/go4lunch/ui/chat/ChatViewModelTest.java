package com.davidvignon.go4lunch.ui.chat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.davidvignon.go4lunch.R;
import com.davidvignon.go4lunch.data.chat_message.ChatMessage;
import com.davidvignon.go4lunch.data.chat_message.ChatMessageRepository;
import com.davidvignon.go4lunch.data.workmate.Workmate;
import com.davidvignon.go4lunch.data.workmate.WorkmateRepository;
import com.davidvignon.go4lunch.utils.LiveDataTestUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class ChatViewModelTest {

    private static final String DEFAULT_CHAT_ID = "DEFAULT_CHAT_ID";
    private static final String DEFAULT_MESSAGE_ID = "DEFAULT_MESSAGE_ID";
    private static final String DEFAULT_MESSAGE = "DEFAULT_MESSAGE";
    private static final String DEFAULT_TIME = "DEFAULT_TIME";
    private static final String DEFAULT_WORKMATE_ID = "DEFAULT_WORKMATE_ID";
    private static final String DEFAULT_WORKMATE_NAME = "DEFAULT_WORKMATE_NAME";
    private static final String DEFAULT_WORKMATE_EMAIL = "DEFAULT_WORKMATE_EMAIL";
    private static final String DEFAULT_WORKMATE_PICTURE_PATH = "DEFAULT_WORKMATE_PICTURE_PATH";
    private static final String DEFAULT_USER_ID = "DEFAULT_USER_ID";
    private static final String DEFAULT_USER_NAME = "DEFAULT_USER_NAME";
    private static final String DEFAULT_USER_EMAIL = "DEFAULT_USER_EMAIL";


    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private final Application application = Mockito.mock(Application.class);
    private final WorkmateRepository workmateRepository = Mockito.mock(WorkmateRepository.class);
    private final ChatMessageRepository chatMessageRepository = Mockito.mock(ChatMessageRepository.class);
    private final SavedStateHandle savedStateHandle = Mockito.mock(SavedStateHandle.class);
    private final FirebaseAuth firebaseAuth = Mockito.mock(FirebaseAuth.class);
    private final FirebaseUser firebaseUser = Mockito.mock(FirebaseUser.class);
    private final MutableLiveData<List<ChatMessage>> chatMessageMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<Workmate> workmateMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<ChatViewState> chatViewState = new MutableLiveData<>();


    private ChatViewModel viewModel;

    @Before
    public void setUp() {

        chatViewState.setValue(getChatViewState());
        workmateMutableLiveData.setValue(getWorkmateInfo());
        chatMessageMutableLiveData.setValue(getDefaultMessages());

        Mockito.doReturn(DEFAULT_USER_ID).when(firebaseAuth).getUid();
        Mockito.doReturn(" dd/MM/yyyy").when(application).getString(R.string.date_time_formater);
        Mockito.doReturn(DEFAULT_WORKMATE_ID).when(savedStateHandle).get("KEY_WORKMATE_ID");
        Mockito.doReturn(chatMessageMutableLiveData).when(chatMessageRepository).getChatMessagesLiveData(DEFAULT_WORKMATE_ID);
        Mockito.doReturn(workmateMutableLiveData).when(workmateRepository).getWorkmateInfoLiveData(DEFAULT_WORKMATE_ID);

        Mockito.doReturn(DEFAULT_USER_NAME).when(firebaseUser).getDisplayName();
        Mockito.doReturn(DEFAULT_USER_ID).when(firebaseUser).getUid();
        Mockito.doReturn(DEFAULT_USER_EMAIL).when(firebaseUser).getEmail();
        Mockito.doReturn(firebaseUser).when(firebaseAuth).getCurrentUser();

        viewModel = new ChatViewModel(
            application,
            workmateRepository,
            chatMessageRepository,
            savedStateHandle,
            firebaseAuth);
    }

    @Test
    public void initial_case() {

        ChatViewState viewState = LiveDataTestUtils.getValueForTesting(viewModel.getChatViewStateLiveData());

        assertNotNull(viewState);

    }

    @Test
    public void no_message_viewState_is_null() {
        // When
        workmateMutableLiveData.setValue(null);
        ChatViewState viewState = LiveDataTestUtils.getValueForTesting(viewModel.getChatViewStateLiveData());

        // Then
        assertNull(viewState);
    }

    @Test
    public void no_workmate_viewState_is_null() {
        // When
        chatMessageMutableLiveData.setValue(null);
        ChatViewState viewState = LiveDataTestUtils.getValueForTesting(viewModel.getChatViewStateLiveData());

        // Then
        assertNull(viewState);
    }

    @Test
    public void message_sender_name_depends_of_boolean() {
        boolean isFromWorkmate = true;
        String name;
        if (isFromWorkmate) {
            name = DEFAULT_WORKMATE_NAME;
        } else {
            name = DEFAULT_USER_NAME;
        }

        ChatViewStateItem viewStateItem = new ChatViewStateItem(
            DEFAULT_MESSAGE_ID,
            DEFAULT_WORKMATE_NAME,
            DEFAULT_MESSAGE,
            DEFAULT_TIME,
            isFromWorkmate
        );
        ChatViewStateItem viewStateItemFromWorkmate = new ChatViewStateItem(
            DEFAULT_MESSAGE_ID,
            name,
            DEFAULT_MESSAGE,
            DEFAULT_TIME,
            isFromWorkmate
        );

        ChatViewStateItem viewStateItemFromUser = new ChatViewStateItem(
            DEFAULT_MESSAGE_ID,
            name,
            DEFAULT_MESSAGE,
            DEFAULT_TIME,
            !isFromWorkmate
        );

        assertEquals(viewStateItem, viewStateItemFromWorkmate);
        assertNotEquals(viewStateItem, viewStateItemFromUser);
    }

    @Test
    public void send_message_only_if_not_empty() {
        viewModel.sendMessage(DEFAULT_MESSAGE);
        Mockito.verify(chatMessageRepository).sendMessage(DEFAULT_MESSAGE, DEFAULT_WORKMATE_ID);

        viewModel.sendMessage("");
        Mockito.verify(chatMessageRepository, Mockito.never()).sendMessage("", DEFAULT_WORKMATE_ID);

    }


    public List<ChatMessage> getDefaultMessages() {
        List<ChatMessage> chatMessages = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            chatMessages.add(new ChatMessage(
                DEFAULT_CHAT_ID,
                DEFAULT_USER_ID,
                DEFAULT_WORKMATE_ID,
                DEFAULT_MESSAGE_ID + i,
                i
            ));
        }
        return chatMessages;
    }

    public Workmate getWorkmateInfo() {
        return new Workmate(
            DEFAULT_WORKMATE_ID,
            DEFAULT_WORKMATE_NAME,
            DEFAULT_WORKMATE_EMAIL,
            DEFAULT_WORKMATE_PICTURE_PATH,
            "",
            ""
        );
    }

    public ChatViewState getChatViewState() {
        return new ChatViewState(
            DEFAULT_WORKMATE_NAME,
            DEFAULT_WORKMATE_PICTURE_PATH,
            getDefaultMessagesViewSate()
        );
    }

    public List<ChatViewStateItem> getDefaultMessagesViewSate() {

        List<ChatViewStateItem> viewStateItems = new ArrayList<>();

        viewStateItems.add(new ChatViewStateItem(
            DEFAULT_MESSAGE_ID,
            DEFAULT_WORKMATE_NAME,
            DEFAULT_MESSAGE,
            DEFAULT_TIME,
            true
        ));

        viewStateItems.add(new ChatViewStateItem(
            DEFAULT_MESSAGE_ID + DEFAULT_USER_ID,
            DEFAULT_USER_NAME,
            DEFAULT_MESSAGE,
            DEFAULT_TIME,
            false
        ));

        return viewStateItems;
    }

}
