package fr.steve.leroy.go4lunch.detail;

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
import fr.steve.leroy.go4lunch.repositories.WorkmateDataRepository;

/**
 * Created by Steve LEROY on 05/12/2021.
 */
public class RestaurantDetailViewModel extends ViewModel {
/*
    private MutableLiveData<List<Workmate>> workmates = new MutableLiveData<>();

    public LiveData<List<Workmate>> getWorkmates() {
        return workmates;
    }

    private String workmateId;
    private String placeId;

    public void init() {
        WorkmateDataRepository workmateRepository = WorkmateDataRepository.getInstance();
        workmateId = workmateRepository.getCurrentUserId();
    }

    public void getAllUsers() {
        WorkmateHelper.getAllWorkmates()
                .addOnSuccessListener( queryDocumentSnapshots -> {  //Callback
                    List<Workmate> workmateList = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        Workmate workmateFetched = documentSnapshot.toObject( Workmate.class );
                        if (!Objects.requireNonNull( workmateFetched ).getWorkmateId().equals( workmateId )) {
                            workmateList.add( workmateFetched );
                        }
                    }
                    this.workmates.setValue( workmateList );
                } );
    }
*/
    public void getWorkmatesForRestaurant() {
      /*
        WorkmateHelper.getWorkmatesForRestaurant(restaurantId)
                .addOnSuccessListener( queryDocumentSnapshots -> {  //Callback
                    List<Workmate> workmateList = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        Workmate workmateFetched = documentSnapshot.toObject( Workmate.class );
                        if (!Objects.requireNonNull( workmateFetched ).getPlaceId().equals( placeId )) {
                            workmateList.add( workmateFetched );
                        }
                    }
                    this.workmates.setValue( workmateList );
                } );*/
    }

}
