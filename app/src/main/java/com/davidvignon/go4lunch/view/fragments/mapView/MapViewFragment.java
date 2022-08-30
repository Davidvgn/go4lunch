package com.davidvignon.go4lunch.view.fragments.mapView;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.davidvignon.go4lunch.R;

import org.jetbrains.annotations.NotNull;


public class MapViewFragment extends Fragment {

    public static MapViewFragment newInstance() {
        MapViewFragment fragment = new MapViewFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map_view, container, false);
    }
}
