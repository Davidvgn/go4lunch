package com.davidvignon.go4lunch.ui.chat;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.davidvignon.go4lunch.R;
import com.davidvignon.go4lunch.data.Chat;
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

        viewModel.getChat().observe(this, new Observer<Chat>() {
            @Override
            public void onChanged(Chat chat) {
                binding.chatTvWorkmateName.setText(chat.getWorkmateName());

                binding.chatIb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewModel.sendMessage(binding.chatEtMessage.getText().toString());
                        binding.chatEtMessage.onEditorAction(EditorInfo.IME_ACTION_DONE);
                        binding.chatEtMessage.getText().clear();
                    }
                });

                Glide.with(getApplicationContext())
                    .load(chat.getWorkmatePicture())
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.chatIv);

                adapter.submitList(viewModel.getChatmess().getValue());
            }
        });
    }
}

