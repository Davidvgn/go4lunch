package com.davidvignon.go4lunch.ui.workmates;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.davidvignon.go4lunch.databinding.WorkmatesItemviewBinding;
import com.davidvignon.go4lunch.ui.OnWorkmateClickedListener;

public class WorkmatesAdapter extends ListAdapter<WorkmatesViewState, WorkmatesAdapter.ViewHolder> {

    @NonNull
    private final OnWorkmateClickedListener listener;

    public WorkmatesAdapter(@NonNull OnWorkmateClickedListener listener) {
        super(new ListWorkmatesItemCallBack());
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(WorkmatesItemviewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position), listener);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final WorkmatesItemviewBinding binding;

        public ViewHolder(@NonNull WorkmatesItemviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(WorkmatesViewState item, OnWorkmateClickedListener listener) {
            binding.itemListName.setText(item.getName());

            Glide.with(binding.itemListAvatar.getContext())
                .load(item.getPicturePath())
                .apply(RequestOptions.circleCropTransform())
                .into(binding.itemListAvatar);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onWorkmateClicked(item.getId());
                }
            });
        }
    }

    private static class ListWorkmatesItemCallBack extends DiffUtil.ItemCallback<WorkmatesViewState> {
        @Override
        public boolean areItemsTheSame(@NonNull WorkmatesViewState oldItem, @NonNull WorkmatesViewState newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull WorkmatesViewState oldItem, @NonNull WorkmatesViewState newItem) {
            return oldItem.equals(newItem);
        }
    }
}
