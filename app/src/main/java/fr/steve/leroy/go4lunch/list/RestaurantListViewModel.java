package fr.steve.leroy.go4lunch.list;

import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.maps.model.PlacesSearchResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import fr.steve.leroy.go4lunch.NearbySearch;
import fr.steve.leroy.go4lunch.firebase.WorkmateHelper;
import fr.steve.leroy.go4lunch.model.Workmate;

/**
 * Created by Steve LEROY on 26/09/2021.
 */
public class RestaurantListViewModel extends ViewModel {

    private Executor executor = Executors.newSingleThreadExecutor();
    private Executor mainExecutor = null;


    private MutableLiveData<Pair<List<Workmate>,List<PlacesSearchResult>>> placesSearchResult = new MutableLiveData<>();

    public LiveData<Pair<List<Workmate>,List<PlacesSearchResult>>> getPlacesSearchResult() {
        return placesSearchResult;
    }



    public void init(Executor mainExecutor) {
        this.mainExecutor = mainExecutor;

    }

    public void getAllRestaurants(com.google.maps.model.LatLng latLng) {

        String workmateId = "";

        WorkmateHelper.getAllWorkmates()
                .addOnSuccessListener( queryDocumentSnapshots -> {  //Callback
                    List<Workmate> workmateList = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        Workmate workmateFetched = documentSnapshot.toObject( Workmate.class );
                        if (!Objects.requireNonNull( workmateFetched ).getWorkmateId().equals( workmateId )) {
                            workmateList.add( workmateFetched );
                        }
                    }
                    executor.execute( (() -> {
                        PlacesSearchResult[] placesSearchResults = new NearbySearch().run( latLng ).results;
                        mainExecutor.execute( (() -> {

                            this.placesSearchResult.setValue( new Pair (workmateList, Arrays.asList( placesSearchResults ) ));

                        }) );
                    }) );
                } );

    }
}