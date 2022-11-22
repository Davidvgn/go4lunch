package com.davidvignon.go4lunch.ui.restaurants;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.davidvignon.go4lunch.databinding.RestaurantsFragmentBinding;
import com.davidvignon.go4lunch.ui.OnRestaurantClickedListener;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RestaurantsFragment extends Fragment {

    @NonNull
    public static RestaurantsFragment newInstance() {
        return new RestaurantsFragment();
    }

    private RestaurantsFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = RestaurantsFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RestaurantViewModel viewModel = new ViewModelProvider(this).get(RestaurantViewModel.class);
        RestaurantAdapter adapter = new RestaurantAdapter(new OnRestaurantClickedListener() {
            @Override
            public void onItemClick(String PlaceId) {

            }
        });
        binding.restaurantRv.setAdapter(adapter);

        viewModel.getRestaurantViewStateLiveData().observe(getViewLifecycleOwner(), new Observer<List<RestaurantViewState>>() {
            @Override
            public void onChanged(List<RestaurantViewState> restaurantViewStates) {
                adapter.submitList(restaurantViewStates);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
