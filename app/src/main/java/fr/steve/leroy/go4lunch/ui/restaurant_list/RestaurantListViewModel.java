package fr.steve.leroy.go4lunch.ui.restaurant_list;

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
import fr.steve.leroy.go4lunch.manager.UserManager;
import fr.steve.leroy.go4lunch.model.User;

/**
 * Created by Steve LEROY on 26/09/2021.
 */
public class RestaurantListViewModel extends ViewModel {

    private Executor executor = Executors.newSingleThreadExecutor();
    private Executor mainExecutor = null;

    private UserManager userManager = UserManager.getInstance();

    private MutableLiveData<Pair<List<User>, List<PlacesSearchResult>>> placesSearchResults = new MutableLiveData<>();

    public LiveData<Pair<List<User>, List<PlacesSearchResult>>> getPlacesSearchResults() {
        return placesSearchResults;
    }


    public void init(Executor mainExecutor) {
        this.mainExecutor = mainExecutor;

    }

    public void getAllRestaurants(com.google.maps.model.LatLng latLng) {

        String workmateId = "";

        userManager.getAllUsers()
                .addOnSuccessListener( queryDocumentSnapshots -> {  //Callback
                    List<User> workmateList = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        User workmateFetched = documentSnapshot.toObject( User.class );
                        if (!Objects.requireNonNull( workmateFetched ).getUid().equals( workmateId )) {
                            workmateList.add( workmateFetched );
                        }
                    }
                    executor.execute( (() -> {
                        PlacesSearchResult[] placesSearchResults = new NearbySearch().run( latLng ).results;
                        mainExecutor.execute( (() -> {

                            this.placesSearchResults.setValue( new Pair( workmateList, Arrays.asList( placesSearchResults ) ) );

                        }) );
                    }) );
                } );

    }

}