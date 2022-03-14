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
        }

    }
}
