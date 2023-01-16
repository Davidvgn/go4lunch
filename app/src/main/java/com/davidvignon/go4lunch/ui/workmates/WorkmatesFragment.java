package com.davidvignon.go4lunch.ui.workmates;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.davidvignon.go4lunch.databinding.WorkmatesFragmentBinding;
import com.davidvignon.go4lunch.ui.OnRestaurantClickedListener;
import com.davidvignon.go4lunch.ui.OnWorkmateClickedListener;


import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class WorkmatesFragment extends Fragment {

    public static WorkmatesFragment newInstance() {
        return new WorkmatesFragment();
    }

    private WorkmatesFragmentBinding binding;

    private OnWorkmateClickedListener onWorkmateClickedListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        onWorkmateClickedListener = (OnWorkmateClickedListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = WorkmatesFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        WorkmatesViewModel viewModel = new ViewModelProvider(this).get(WorkmatesViewModel.class);
        WorkmatesAdapter adapter = new WorkmatesAdapter(onWorkmateClickedListener);
        binding.workmatesRv.setAdapter(adapter);

        viewModel.getWorkmatesViewStatesLiveData().observe(getViewLifecycleOwner(), workmatesViewStates -> adapter.submitList(workmatesViewStates));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
