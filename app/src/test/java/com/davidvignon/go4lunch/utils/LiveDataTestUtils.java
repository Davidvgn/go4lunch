package com.davidvignon.go4lunch.utils;

import static org.junit.Assert.fail;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

public class LiveDataTestUtils {
//    public static <T> T getValueForTesting(@NonNull final LiveData<T> liveData) {
//        liveData.observeForever(new Observer<T>() {
//            @Override
//            public void onChanged(T t) {
//            }
//        });
//
//        return liveData.getValue();
//    }
public static <T> void observeForTesting(LiveData<T> liveData, OnObservedListener<T> onObservedListener) {
    boolean[] called = {false};

    liveData.observeForever(value -> {
        called[0] = true;
        onObservedListener.onObserved(value);
    });

    if (!called[0]) {
        fail("LiveData was not called");
    }
}

    public interface OnObservedListener<T> {
        void onObserved(T value);
    }
}
