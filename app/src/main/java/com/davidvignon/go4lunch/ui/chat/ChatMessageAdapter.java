package com.davidvignon.go4lunch.ui.chat;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.davidvignon.go4lunch.databinding.ChatItemviewBinding;

public class ChatMessageAdapter extends ListAdapter<ChatMessageViewState, ChatMessageAdapter.ViewHolder> {

    public ChatMessageAdapter() {
        super(new ListChatMessageItemCallBack());
    }

    @NonNull
    @Override
    public ChatMessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ChatItemviewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ChatItemviewBinding binding;

        public ViewHolder(@NonNull ChatItemviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ChatMessageViewState item) {

            binding.chatRcTv.setText(item.getMessage());
            binding.chatRcTvMessageDate.setText(item.getTime());
            binding.chatRcTvMessageAuthor.setText(item.getWorkmateName());
        }
    }

    private static class ListChatMessageItemCallBack extends DiffUtil.ItemCallback<ChatMessageViewState> {
        @Override
        public boolean areItemsTheSame(@NonNull ChatMessageViewState oldItem, @NonNull ChatMessageViewState newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull ChatMessageViewState oldItem, @NonNull ChatMessageViewState newItem) {
            return oldItem.equals(newItem);
        }
    }
}

