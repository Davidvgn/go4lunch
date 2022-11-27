package com.davidvignon.go4lunch.ui.workmates;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.davidvignon.go4lunch.databinding.WorkmatesFragmentBinding;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class WorkmatesFragment extends Fragment {

    private WorkmatesFragmentBinding binding;

    public static WorkmatesFragment newInstance() {
        return new WorkmatesFragment();
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
        WorkmatesAdapter adapter = new WorkmatesAdapter();
        binding.workmatesRv.setAdapter(adapter);

//        viewModel.getWorkmatesViewStatesLiveData().observe(getViewLifecycleOwner(), new Observer<List<WorkmatesViewStates>>() {
//            @Override
//            public void onChanged(List<WorkmatesViewStates> workmatesViewStates) {
//                adapter.submitList(workmatesViewStates);
//            }
//        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
