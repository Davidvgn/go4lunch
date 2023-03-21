package com.davidvignon.go4lunch.data;

import android.app.Application;
import android.content.Context;
import android.os.Looper;

import androidx.work.WorkManager;

import com.davidvignon.go4lunch.BuildConfig;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
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
    public @interface PlacesApi {}

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    public @interface DetailsPlacesApi {}

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    public @interface AutoCompleteApi {}

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
    @PlacesApi
    public com.davidvignon.go4lunch.data.google_places.PlacesApi providePlacesApi() {
        Gson gson = new GsonBuilder().setLenient().create();
        OkHttpClient httpClient = new OkHttpClient.Builder().build();
        String baseUrl = "https://maps.googleapis.com/";

        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

        return retrofit.create(com.davidvignon.go4lunch.data.google_places.PlacesApi.class);
    }

    @Singleton
    @Provides
    @DetailsPlacesApi
    public com.davidvignon.go4lunch.data.google_places.PlacesApi providePlaceDetailsApi() {
        Gson gson = new GsonBuilder().setLenient().create();
        OkHttpClient httpClient = new OkHttpClient.Builder().build();
        String baseUrl = "https://maps.googleapis.com/";

        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

        return retrofit.create(com.davidvignon.go4lunch.data.google_places.PlacesApi.class);
    }

    @Singleton
    @Provides
    @AutoCompleteApi
    public com.davidvignon.go4lunch.data.google_places.PlacesApi provideAutoCompleteApi() {
        Gson gson = new GsonBuilder().setLenient().create();
        OkHttpClient httpClient = new OkHttpClient.Builder().build();
        String baseUrl = "https://maps.googleapis.com/";

        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

        return retrofit.create(com.davidvignon.go4lunch.data.google_places.PlacesApi.class);
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

    @Singleton
    @Provides
    public PlacesClient providePlacesClient(Application application) {
        if (!Places.isInitialized()) {
            Places.initialize(application, BuildConfig.MAPS_API_KEY);
        }
        return Places.createClient(application);
    }


}
