package fr.steve.leroy.go4lunch.firebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import fr.steve.leroy.go4lunch.model.Booking;

/**
 * Created by Steve LEROY on 21/01/2022.
 */
public class RestaurantHelper {

    public static final String COLLECTION_BOOKING = "booking";
    private static final String COLLECTION_LIKED_RESTAURANT = "likedRestaurant";

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getBookingCollection() {
        return FirebaseFirestore.getInstance().collection( COLLECTION_BOOKING );
    }

    public static CollectionReference getLikedCollection() {
        return FirebaseFirestore.getInstance().collection( COLLECTION_LIKED_RESTAURANT );
    }


    // --- CREATE ---

    public static Task<Void> createBooking(String uid, String username, String placeId, String restaurantName) {
        Booking bookingToCreate = new Booking( uid, username,  placeId, restaurantName );
        return RestaurantHelper.getBookingCollection().document( uid ).set( bookingToCreate );
    }

    public static Task<Void> createLike(String placeId, String restaurantName, String uid, String username) {
        Map<String, Object> like = new HashMap<>();
        like.put( "placeId", placeId );
        like.put( "restaurantName", restaurantName );
        like.put( "uid", uid );
        like.put( "username", username );
        return RestaurantHelper.getLikedCollection().document( placeId + restaurantName + uid + username ).set( like, SetOptions.merge() );
    }


    // --- GET ---

    public static Task<QuerySnapshot> getBooking(String userId, Date date) {
        return RestaurantHelper.getBookingCollection().whereEqualTo( "workmateId", userId ).whereEqualTo( "dateCreated", date ).get();
    }

    public static Task<QuerySnapshot> getTodayBooking(String placeId, String bookingDate) {
        return RestaurantHelper.getBookingCollection().whereEqualTo( "placeId", placeId ).whereEqualTo( "dateCreated", bookingDate ).get();
    }

    public static Task<DocumentSnapshot> getLikeForThisRestaurant(String placeId) {
        //TODO : changer la façon de récupérer le like
        return RestaurantHelper.getLikedCollection().document( placeId ).get();
    }

    public static Task<QuerySnapshot> getAllLikeByUserId(String workmateId) {
        return RestaurantHelper.getLikedCollection().whereEqualTo( workmateId, true ).get();
    }


    // --- UPDATE ---
    public static Task<Void> updateBooking(String uid, String username, String placeId, String restaurantName) {
        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put( "username", username );
        updatedData.put( "placeId", placeId );
        updatedData.put( "restaurantName", restaurantName );
        return RestaurantHelper.getBookingCollection().document( uid ).update( updatedData );
    }


    // --- DELETE ---

    public static Task<Void> deleteBooking(String bookingId) {
        return RestaurantHelper.getBookingCollection().document( bookingId ).delete();
    }

    public static Boolean deleteLike(String placeId, String uid) {
        RestaurantHelper.getLikeForThisRestaurant( placeId ).addOnCompleteListener( restaurantTask -> {
            if (restaurantTask.isSuccessful()) {
                Map<String, Object> update = new HashMap<>();
                update.put( uid, FieldValue.delete() );
                RestaurantHelper.getLikedCollection().document( placeId ).update( update );
            }
        } );
        return true;
    }
}