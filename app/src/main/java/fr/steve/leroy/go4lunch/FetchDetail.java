package fr.steve.leroy.go4lunch;

import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.PlaceDetails;

import java.io.IOException;

/**
 * Created by Steve LEROY on 19/12/2021.
 */
public class FetchDetail {

    public PlaceDetails run(String placeId) {
        PlaceDetails request = new PlaceDetails();
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey( "AIzaSyDZVL23At9zWQeYZsUd2sx7A3GaqPhkZEw" )
                .build();

        try {
            request = PlacesApi.placeDetails( context, placeId )
                    .await();
        } catch (ApiException | IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            return request;
        }

    }
}


