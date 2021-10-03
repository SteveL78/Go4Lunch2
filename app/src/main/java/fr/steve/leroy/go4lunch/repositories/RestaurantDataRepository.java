package fr.steve.leroy.go4lunch.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

/**
 * Created by Steve LEROY on 26/09/2021.
 */
public class RestaurantDataRepository {
/*
    private int PROXIMITY_RADIUS = 5000;

    public MutableLiveData<List<Result>> getRestaurants(android.location.Location location) {

        MutableLiveData<List<Result>> results = new MutableLiveData<>();

        // 2.2 - Get a Retrofit instance and the related endpoints
        RetrofitMaps mapsService = RetrofitMaps.retrofit.create(RetrofitMaps.class);

        // 2.3 - Create the call on the API
        Call<NearbySearch> call = mapsService.getNearbyPlaces("restaurant", location.getLatitude() + "," + location.getLongitude(), PROXIMITY_RADIUS);
        // 2.4 - Start the call
        call.enqueue(new Callback<NearbySearch>() {

            @Override
            public void onResponse(Response<NearbySearch> response, Retrofit retrofit) {
                results.setValue(response.body().getResults());


            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
        return results;
    }
*/

}
