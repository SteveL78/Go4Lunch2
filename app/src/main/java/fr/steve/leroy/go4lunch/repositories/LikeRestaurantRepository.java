package fr.steve.leroy.go4lunch.repositories;

import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import fr.steve.leroy.go4lunch.R;
import fr.steve.leroy.go4lunch.firebase.BookingHelper;
import fr.steve.leroy.go4lunch.firebase.LikeHelper;
import fr.steve.leroy.go4lunch.ui.restaurant_details.RestaurantDetailActivity;

/**
 * Created by Steve LEROY on 20/03/2022.
 */
public class LikeRestaurantRepository {

    private static final String COLLECTION_NAME = "likesRestaurants";
    private static final String USER_ID_FIELD = "uid";
    private static final String USERNAME_FIELD = "username";
    private static final String PLACE_ID_FIELD = "placeId";
    private static final String RESTAURANT_NAME_FIELD = "restaurantName";


    private static volatile LikeRestaurantRepository instance;

    private LikeRestaurantRepository() {
    }

    public static LikeRestaurantRepository getInstance() {
        LikeRestaurantRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized (LikeRestaurantRepository.class) {
            if (instance == null) {
                instance = new LikeRestaurantRepository();
            }
            return instance;
        }
    }


    /*********************
     *     FIRESTORE     *
     ********************/

    // Get the Collection Reference
    private CollectionReference getLikesRestaurantsCollection() {
        return FirebaseFirestore.getInstance().collection( COLLECTION_NAME );
    }

    // --- GET ---
    public static Task<QuerySnapshot> getLike(String uid, String placeId) {
        return BookingHelper.getBookingCollection().whereEqualTo( USER_ID_FIELD, uid ).whereEqualTo( PLACE_ID_FIELD, placeId ).get();
    }


    // Create a Like in Firestore
    /*
    public void createLike(String uid, String username, String placeId, String restaurantName) {
        LikeHelper.createLike( uid, username, placeId, restaurantName ).addOnFailureListener( onFailureListener() );
        displayFloating( (R.drawable.ic_baseline_clear_orange_24) );
        Update_Booking_RecyclerView( mPlaceDetails.placeId, mPlaceDetails.name );
        Toast.makeText( RestaurantDetailActivity.this, R.string.new_booking, Toast.LENGTH_SHORT ).show();
    }




    // Delete a Like in Firestore
    public void deleteLike(String placeId, String uid ) {

    }
*/

}
