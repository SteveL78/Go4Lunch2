package fr.steve.leroy.go4lunch.api;

import fr.steve.leroy.go4lunch.BuildConfig;
import fr.steve.leroy.go4lunch.entities.APIDetailsRestaurantResponse;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
/**
 * Created by Steve LEROY on 10/11/2021.
 */
public interface APIDetails {

    @GET("place/details/json?&fields=vicinity,name,place_id,id,geometry,opening_hours,international_phone_number,website,rating,photo&key=" + BuildConfig.ApiPlaceKey)
    Observable<APIDetailsRestaurantResponse> getRestaurantDetails(@Query("place_id") String placeId);

}

