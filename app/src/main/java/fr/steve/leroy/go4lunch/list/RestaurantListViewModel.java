package fr.steve.leroy.go4lunch.list;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.maps.model.PlacesSearchResult;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import fr.steve.leroy.go4lunch.NearbySearch;

/**
 * Created by Steve LEROY on 26/09/2021.
 */
public class RestaurantListViewModel extends ViewModel {


    private Executor executor = Executors.newSingleThreadExecutor();
    private Executor mainExecutor = null;

    private MutableLiveData<List<PlacesSearchResult>> placesSearchResult = new MutableLiveData<>();

    public LiveData<List<PlacesSearchResult>> getPlacesSearchResult() {
        return placesSearchResult;
    }



    public void getAllRestaurants( com.google.maps.model.LatLng latLng ) {

        executor.execute( (() -> {
            PlacesSearchResult[] placesSearchResults = new NearbySearch().run(latLng).results;
            mainExecutor.execute( (() -> {

                this.placesSearchResult.setValue( Arrays.asList( placesSearchResults ) );


            }) );
        }) );


    }
}
