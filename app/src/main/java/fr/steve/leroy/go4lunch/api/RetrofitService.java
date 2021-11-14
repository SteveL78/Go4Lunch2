package fr.steve.leroy.go4lunch.api;

import fr.steve.leroy.go4lunch.BuildConfig;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Steve LEROY on 14/11/2021.
 */
public class RetrofitService {

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl( BuildConfig.ApiPlaceBase)
            .addConverterFactory( GsonConverterFactory.create())
            .addCallAdapterFactory( RxJava3CallAdapterFactory.create())
            .build();

    public static APIInterface getInterface() {
        return retrofit.create(APIInterface.class);
    }

    public static APIDetails getDetails() {return retrofit.create(APIDetails.class);}
}
