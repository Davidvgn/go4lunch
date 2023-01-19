package com.davidvignon.go4lunch.ui.chat;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.davidvignon.go4lunch.databinding.ChatActivityBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ChatActivity extends AppCompatActivity {

    //todo david : add button to see which restaurant is choosen by workmate

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ChatActivityBinding binding = ChatActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ChatMessageAdapter adapter = new ChatMessageAdapter();
        binding.chatRv.setAdapter(adapter);

        ChatViewModel viewModel = new ViewModelProvider(this).get(ChatViewModel.class);

        viewModel.getChatViewStateLiveData().observe(this, new Observer<ChatViewState>() {
            @Override
            public void onChanged(ChatViewState chatViewState) {
                binding.chatTvWorkmateName.setText(chatViewState.getWorkmateName());

                binding.chatIb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewModel.sendMessage(binding.chatEtMessage.getText().toString());
                        binding.chatEtMessage.onEditorAction(EditorInfo.IME_ACTION_DONE);
                        binding.chatEtMessage.getText().clear();
                    }
                });

                Glide.with(getApplicationContext())
                    .load(chatViewState.getWorkmatePicture())
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.chatIv);

                adapter.submitList(chatViewState.getMessages());
            }
        });
    }
}

