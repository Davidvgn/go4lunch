package com.davidvignon.go4lunch.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CurrentSearchRepository {

   private final MutableLiveData<String> query = new MutableLiveData<>();

    @Inject
    public CurrentSearchRepository() {
    }

    public void setOnSearchRestaurantSelected(String text){
         query.setValue(text);

    }
    public LiveData<String> getOnSearchRestaurantSelected(){
        return query;
    }
}
