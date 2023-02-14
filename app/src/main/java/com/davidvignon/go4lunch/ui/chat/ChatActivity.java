package com.davidvignon.go4lunch.ui.chat;

import android.os.Bundle;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.davidvignon.go4lunch.databinding.ChatActivityBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ChatActivityBinding binding = ChatActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ChatMessageAdapter adapter = new ChatMessageAdapter();
        binding.chatRv.setAdapter(adapter);

        ChatViewModel viewModel = new ViewModelProvider(this).get(ChatViewModel.class);

        viewModel.getChatViewStateLiveData().observe(this, chatViewState -> {
            binding.chatTvWorkmateName.setText(chatViewState.getWorkmateName());

            binding.chatIb.setOnClickListener(view -> {
                viewModel.sendMessage(binding.chatEtMessage.getText().toString());
                binding.chatEtMessage.onEditorAction(EditorInfo.IME_ACTION_DONE);
                binding.chatEtMessage.getText().clear();
            });

            Glide.with(getApplicationContext())
                .load(chatViewState.getWorkmatePicture())
                .apply(RequestOptions.circleCropTransform())
                .into(binding.chatIv);

            adapter.submitList(chatViewState.getMessages());
        });
    }
}

