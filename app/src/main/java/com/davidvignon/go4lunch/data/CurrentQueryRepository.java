package com.davidvignon.go4lunch.data;

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

    public void setCurrentRestaurantQuery(String query){
        queryMutableLiveData.setValue(query);
    }

    public LiveData<String> getCurrentRestaurantQuery(){
        return queryMutableLiveData;
    }
}
