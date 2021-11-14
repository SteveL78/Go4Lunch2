package fr.steve.leroy.go4lunch;

import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.RankBy;

import java.io.IOException;

/**
 * Created by Steve LEROY on 19/09/2021.
 */
public class NearbySearch {

    public PlacesSearchResponse run(LatLng latLng) {
        PlacesSearchResponse request = new PlacesSearchResponse();
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey( "AIzaSyDZVL23At9zWQeYZsUd2sx7A3GaqPhkZEw" )
                .build();
        // LatLng location = new LatLng(48.8566, 2.3522);

        try {
            request = PlacesApi.nearbySearchQuery( context, latLng )
                    .radius( 500 )
                    .rankby( RankBy.PROMINENCE )
                    .type( PlaceType.RESTAURANT )
                    .await();
        } catch (ApiException | IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            return request;

/*
            List<Restaurant> restaurantList = new ArrayList<Restaurant>();
            for (PlacesSearchResult result : request.results) {
                Restaurant restaurant = new Restaurant( result.placeId, result.name, result.formattedAddress, result.openingHours, result.vicinity, result.photos );
                restaurantList.add( restaurant );
            }
            return restaurantList;
        }
*/

        }




    /*
    OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
    Request request = new Request.Builder()
            .url("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522%2C151.1957362&radius=1500&type=restaurant&keyword=restaurant&key=AIzaSyDZVL23At9zWQeYZsUd2sx7A3GaqPhkZEw")
            .method("GET", null)
            .build();
    Response response = client.newCall(request).execute();
*/


    }
}
