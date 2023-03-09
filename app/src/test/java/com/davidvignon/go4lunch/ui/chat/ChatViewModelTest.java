package com.davidvignon.go4lunch.ui.chat;

import static net.bytebuddy.matcher.ElementMatchers.any;
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

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ChatViewModelTest {


    private static final String DEFAULT_CHAT_ID = "DEFAULT_CHAT_ID";
    private static final String DEFAULT_USER_NAME = "DEFAULT_USER_NAME";
    private static final String DEFAULT_WORKMATE_ID = "DEFAULT_WORKMATE_ID";
    private static final String DEFAULT_WORKMATE_NAME = "DEFAULT_WORKMATE_NAME";
    private static final String DEFAULT_WORKMATE_EMAIL = "DEFAULT_WORKMATE_EMAIL";
    private static final String DEFAULT_WORKMATE_PICTURE_PATH = "DEFAULT_WORKMATE_PICTURE_PATH";
    private static final String DEFAULT_USER_ID = "DEFAULT_USER_ID";
    private static final String DEFAULT_MESSAGE_ID = "DEFAULT_MESSAGE_ID";

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

        chatViewState.setValue(getChatVS());
        workmateMutableLiveData.setValue(getWorkmateInfo());
        chatMessageMutableLiveData.setValue(getDefaultMessages());

        Mockito.doReturn(DEFAULT_USER_ID).when(firebaseAuth).getUid();
        Mockito.doReturn(" dd/MM/yyyy").when(application).getString(R.string.date_time_formater);
        Mockito.doReturn(DEFAULT_WORKMATE_ID).when(savedStateHandle).get("KEY_WORKMATE_ID");
        Mockito.doReturn(chatMessageMutableLiveData).when(chatMessageRepository).getChatMessagesLiveData(DEFAULT_WORKMATE_ID);
        Mockito.doReturn(workmateMutableLiveData).when(workmateRepository).getWorkmateInfoLiveData(DEFAULT_WORKMATE_ID);
        //todo david PLS

        Mockito.doReturn("user").when(firebaseUser).getDisplayName();
        Mockito.doReturn("user123").when(firebaseUser).getUid();
        Mockito.doReturn("user123@gmail.com").when(firebaseUser).getEmail();
        Mockito.doReturn(firebaseUser).when(firebaseAuth).getCurrentUser().getDisplayName();

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
        Workmate workmate = new Workmate(
            DEFAULT_WORKMATE_ID,
            DEFAULT_WORKMATE_NAME,
            DEFAULT_WORKMATE_EMAIL,
            DEFAULT_WORKMATE_PICTURE_PATH,
            null,
            null
        );
        return workmate;
    }

    public ChatViewState getChatVS() {
        ChatViewState viewState = new ChatViewState(
            DEFAULT_WORKMATE_NAME,
            DEFAULT_WORKMATE_PICTURE_PATH,
            getDefaultMessagesViewSate()
        );

        return viewState;
    }

    public List<ChatViewStateItem> getDefaultMessagesViewSate() {
        List<ChatViewStateItem> viewStateItems = new ArrayList<>();

        viewStateItems.add(new ChatViewStateItem(
            "ID",
            DEFAULT_WORKMATE_NAME,
            "DEFAULT_MESSAGE",
            "time",
            true
        ));

        return viewStateItems;

    }

}
