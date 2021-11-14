package fr.steve.leroy.go4lunch.api;

import fr.steve.leroy.go4lunch.BuildConfig;
import fr.steve.leroy.go4lunch.entities.APIRestaurantResponse;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Steve LEROY on 14/11/2021.
 */
public interface APIInterface {

    @GET("place/nearbysearch/json?type=restaurant&rankby=distance&key=" + BuildConfig.ApiPlaceKey)
    Observable<APIRestaurantResponse> getRestaurants(@Query("location") String location);
}
