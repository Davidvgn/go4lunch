package com.davidvignon.go4lunch.ui.main.predictions;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.davidvignon.go4lunch.databinding.PredictionItemviewBinding;
import com.davidvignon.go4lunch.ui.OnPredictionClickedListener;


public class PredictionsAdapter extends ListAdapter<PredictionViewState, PredictionsAdapter.ViewHolder> {

    @NonNull
    private final OnPredictionClickedListener listener;

    public PredictionsAdapter(@NonNull OnPredictionClickedListener listener) {
        super(new ListPredictionItemCallBack());
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(PredictionItemviewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position), listener);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final PredictionItemviewBinding binding;

        public ViewHolder(@NonNull PredictionItemviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(PredictionViewState item, OnPredictionClickedListener listener) {
            binding.predictionItemTextViewName.setText(item.getDescription());
            binding.predictionItemClRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onPredictionClickedListener(item.getPlaceId(), item.getDescription());
                }
            });

        }
    }


    private static class ListPredictionItemCallBack extends DiffUtil.ItemCallback<PredictionViewState> {
        @Override
        public boolean areItemsTheSame(@NonNull PredictionViewState oldItem, @NonNull PredictionViewState newItem) {
            return oldItem.getPlaceId().equals(newItem.getPlaceId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull PredictionViewState oldItem, @NonNull PredictionViewState newItem) {
            return oldItem.equals(newItem);
        }
    }
}
