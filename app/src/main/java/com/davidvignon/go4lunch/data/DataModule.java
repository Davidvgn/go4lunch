package com.davidvignon.go4lunch.data;

import android.content.Context;
import android.os.Looper;

import androidx.work.WorkManager;

import com.davidvignon.go4lunch.data.google_places.PlacesApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


@Module
@InstallIn(SingletonComponent.class)
public class DataModule {

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    public @interface PlacesAPi {}

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    public @interface DetailsPlacesAPi {}

    @Singleton
    @Provides
    public FusedLocationProviderClient provideFusedLocationProviderClient(@ApplicationContext Context context) {
        return LocationServices.getFusedLocationProviderClient(context);
    }

    @Singleton
    @Provides
    public FirebaseAuth provideFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    @Singleton
    @Provides
    @PlacesAPi
    public PlacesApi providePlacesApi() {
        Gson gson = new GsonBuilder().setLenient().create();
        OkHttpClient httpClient = new OkHttpClient.Builder().build();
        String baseUrl = "https://maps.googleapis.com/";

        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

        return retrofit.create(PlacesApi.class);
    }

    @Singleton
    @Provides
    @DetailsPlacesAPi
    public PlacesApi providePlaceDetailsApi() {
        Gson gson = new GsonBuilder().setLenient().create();
        OkHttpClient httpClient = new OkHttpClient.Builder().build();
        String baseUrl = "https://maps.googleapis.com/";

        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

        return retrofit.create(PlacesApi.class);
    }

    @Singleton
    @Provides
    public FirebaseFirestore provideFirestoreDb() {
        return FirebaseFirestore.getInstance();
    }

    @Provides
    public Looper provideLooper() {
        return Looper.getMainLooper();
    }

    @Singleton
    @Provides
    public WorkManager provideWorkManager(@ApplicationContext Context context) {
        return WorkManager.getInstance(context);
    }

}
