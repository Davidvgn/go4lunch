package com.davidvignon.go4lunch.ui.chat;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.davidvignon.go4lunch.databinding.ChatItemviewBinding;

public class ChatMessageAdapter extends ListAdapter<ChatViewStateItem, RecyclerView.ViewHolder> {

    enum ViewType {
        CURRENT_USER,
        OTHER_USER
    }

    public ChatMessageAdapter() {
        super(new ListChatMessageItemCallBack());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (ViewType.values()[viewType]) {
            case CURRENT_USER:
                return new CurrentUserViewHolder(
                    ChatItemviewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false)
                );
            case OTHER_USER:
            default:
                return new OtherUserViewHolder(
                    ChatItemviewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false)
                );
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CurrentUserViewHolder) {
            ((CurrentUserViewHolder) holder).bind(getItem(position));
        } else if (holder instanceof OtherUserViewHolder) {
            ((OtherUserViewHolder) holder).bind(getItem(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        ChatViewStateItem item = getItem(position);

        if (item.isFromCurrentUser()) {
            return ViewType.CURRENT_USER.ordinal();
        } else {
            return ViewType.OTHER_USER.ordinal();
        }
    }

    public static class CurrentUserViewHolder extends RecyclerView.ViewHolder {
        private final ChatItemviewBinding binding;

        public CurrentUserViewHolder(@NonNull ChatItemviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ChatViewStateItem item) {

            binding.chatRcTv.setText(item.getMessage());
            binding.chatRcTvMessageDate.setText(item.getTime());
            binding.chatRcTvMessageAuthor.setText(item.getWorkmateName());
        }
    }

    public static class OtherUserViewHolder extends RecyclerView.ViewHolder {
        private final ChatItemviewBinding binding;

        public OtherUserViewHolder(@NonNull ChatItemviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ChatViewStateItem item) {

            binding.chatRcTv.setText(item.getMessage());
            binding.chatRcTvMessageDate.setText(item.getTime());
            binding.chatRcTvMessageAuthor.setText(item.getWorkmateName());
        }
    }

    private static class ListChatMessageItemCallBack extends DiffUtil.ItemCallback<ChatViewStateItem> {
        @Override
        public boolean areItemsTheSame(@NonNull ChatViewStateItem oldItem, @NonNull ChatViewStateItem newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull ChatViewStateItem oldItem, @NonNull ChatViewStateItem newItem) {
            return oldItem.equals(newItem);
        }
    }
}

