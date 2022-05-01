package fr.steve.leroy.go4lunch.ui.restaurant_details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import fr.steve.leroy.go4lunch.firebase.BookingHelper;
import fr.steve.leroy.go4lunch.manager.UserManager;
import fr.steve.leroy.go4lunch.model.Like;
import fr.steve.leroy.go4lunch.model.User;
import fr.steve.leroy.go4lunch.repositories.LikeRestaurantRepository;

/**
 * Created by Steve LEROY on 05/12/2021.
 */
public class RestaurantDetailViewModel extends ViewModel {

    private Executor executor = Executors.newSingleThreadExecutor();
    private Executor mainExecutor = null;

    private UserManager userManager = UserManager.getInstance();

    private MutableLiveData<Boolean> like = new MutableLiveData<>();
    private MutableLiveData<List<User>> userList = new MutableLiveData<>();

    public LiveData<Boolean> getLike() {
        return like;
    }
    public LiveData<List<User>> getUserList() {
        return userList;
    }

    public void init(Executor mainExecutor) {
        this.mainExecutor = mainExecutor;
    }


    public void getLike(String uid, String placeId) {

        LikeRestaurantRepository.getLike( uid, placeId )
                .addOnSuccessListener( queryDocumentSnapshots -> {  //Callback
                    List<Like> likeList = new ArrayList<>();
                    if (queryDocumentSnapshots.getDocuments().size() >= 1) {
                        this.like.setValue( true );
                    } else {
                        this.like.setValue( false );
                    }



                    /*
                    executor.execute( (() -> {
                        PlacesSearchResult[] placesSearchResults = new NearbySearch().run( latLng ).results;
                        mainExecutor.execute( (() -> {

                            this.placesSearchResults.setValue( new Pair( workmateList, Arrays.asList( placesSearchResults ) ) );

                        }) );
                    }) );

                     */
                } );

    }

}
