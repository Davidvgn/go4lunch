package com.davidvignon.go4lunch.data;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CurrentQueryRepository {

   private final MutableLiveData<String> queryMutableLiveData = new MutableLiveData<>();

    @Inject
    public CurrentQueryRepository() {
    }

    public void setCurrentRestaurantQuery(@Nullable String query){
        queryMutableLiveData.setValue(query);
    }

    public LiveData<String> getCurrentRestaurantQuery(){
        return queryMutableLiveData;
    }
}
