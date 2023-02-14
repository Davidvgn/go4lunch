package com.davidvignon.go4lunch.ui.workmates;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.davidvignon.go4lunch.R;
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

            if (item.getSelectedRestaurant() != null) {
                binding.itemListName.setTypeface(null, Typeface.NORMAL);

                binding.itemListName.setTextColor(Color.BLACK);

//                binding.itemListSentence.setText(R.string.is_eating_at);
                binding.itemListSentence.setText(R.string.is_eating_at);

                binding.itemListRestaurantName.setText(item.getSelectedRestaurantName());
                binding.itemListRestaurantName.setTextColor(Color.BLACK);


                binding.itemListSentence.setTypeface(null, Typeface.NORMAL);
                binding.itemListSentence.setTextColor(Color.BLACK);
            } else {
                binding.itemListName.setTypeface(null, Typeface.ITALIC);

                binding.itemListSentence.setText(R.string.hasnt_decide_yet);
                binding.itemListSentence.setTypeface(null, Typeface.ITALIC);
            }

            Glide.with(binding.itemListAvatar.getContext())
                .load(item.getPicturePath())
                .apply(RequestOptions.circleCropTransform())
                .into(binding.itemListAvatar);
            itemView.setOnClickListener(view -> listener.onWorkmateClicked(item.getId()));
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
