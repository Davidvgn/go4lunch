package com.davidvignon.go4lunch.view.fragments.workMates;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.davidvignon.go4lunch.R;

import org.jetbrains.annotations.NotNull;

public class WorkMatesFragment extends Fragment {

    public static WorkMatesFragment newInstance() {
        WorkMatesFragment fragment = new WorkMatesFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_workmates, container, false);
    }
}
