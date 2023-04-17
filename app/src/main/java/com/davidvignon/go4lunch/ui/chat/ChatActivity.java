package com.davidvignon.go4lunch.ui.chat;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.davidvignon.go4lunch.R;
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
        setSupportActionBar(binding.chatToolbar);
        binding.chatToolbar.setTitle(R.string.chat_activity_title);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, true);
        binding.chatRv.setLayoutManager(layoutManager);



        ChatViewModel viewModel = new ViewModelProvider(this).get(ChatViewModel.class);

        viewModel.getChatViewStateLiveData().observe(this, chatViewState -> {
            binding.chatTvWorkmateName.setText(chatViewState.getWorkmateName());

            binding.chatEtMessage.setOnEditorActionListener((textView, i, keyEvent) -> {
                if (binding.chatEtMessage.getText() != null) {
                    viewModel.sendMessage(binding.chatEtMessage.getText().toString());
                }
                binding.chatEtMessage.getText().clear();
                return true;
            });

            viewModel.getShowToastSingleLiveEvent().observe(this, message ->
                Toast.makeText(ChatActivity.this, message, Toast.LENGTH_SHORT).show()
            );

            viewModel.getIsMessageSentValueLiveData().observe(this, aBoolean -> closeKeyboard());

            Glide.with(getApplicationContext())
                .load(chatViewState.getWorkmatePicture())
                .apply(RequestOptions.circleCropTransform())
                .into(binding.chatIv);

            adapter.submitList(chatViewState.getMessages());
        });
    }


    private void closeKeyboard() {
        View view = this.getCurrentFocus();
            InputMethodManager manager
                = (InputMethodManager)
                getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            manager
                .hideSoftInputFromWindow(
                    view.getWindowToken(), 0);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

