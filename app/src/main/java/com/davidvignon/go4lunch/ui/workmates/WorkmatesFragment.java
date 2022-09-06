package com.davidvignon.go4lunch.ui.workmates;

import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.davidvignon.go4lunch.R;
import com.davidvignon.go4lunch.databinding.WorkmatesFragmentBinding;

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
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
