package com.davidvignon.go4lunch.ui.workmates;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.davidvignon.go4lunch.databinding.WorkmatesItemviewBinding;

public class WorkmatesAdapter extends ListAdapter<WorkmatesViewStates, WorkmatesAdapter.ViewHolder> {

    public WorkmatesAdapter() {
        super(new ListWorkmatesItemCallBack());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(WorkmatesItemviewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final WorkmatesItemviewBinding binding;

        public ViewHolder(@NonNull WorkmatesItemviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(WorkmatesViewStates item) {
            binding.itemListName.setText(item.getName());

            Glide.with(binding.itemListAvatar.getContext())
                .load(item.getPicturePath())
                .apply(RequestOptions.circleCropTransform())
                .into(binding.itemListAvatar);
        }
    }

    private static class ListWorkmatesItemCallBack extends DiffUtil.ItemCallback<WorkmatesViewStates> {
        @Override
        public boolean areItemsTheSame(@NonNull WorkmatesViewStates oldItem, @NonNull WorkmatesViewStates newItem) {
            return oldItem.getName().equals(newItem.getName());
        }

        @Override
        public boolean areContentsTheSame(@NonNull WorkmatesViewStates oldItem, @NonNull WorkmatesViewStates newItem) {
            return oldItem.equals(newItem);
        }
    }

}
