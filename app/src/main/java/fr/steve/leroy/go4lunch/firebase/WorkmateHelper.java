package fr.steve.leroy.go4lunch.firebase;


import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import fr.steve.leroy.go4lunch.model.Booking;

/**
 * Created by Steve LEROY on 03/10/2021.
 */
public class WorkmateHelper {

    private static final String COLLECTION_NAME = "workmate";
    private static final String COLLECTION_LIKED_NAME = "restaurantLiked";

    // ----- COLLECTION REFERENCE -----
    public static CollectionReference getWorkmatesCollection() {
        return FirebaseFirestore.getInstance().collection( COLLECTION_NAME );
    }

    public static CollectionReference getLikedCollection() {
        return FirebaseFirestore.getInstance().collection( COLLECTION_LIKED_NAME );
    }

    // --- CREATE ---

    public static Task<DocumentReference> createBooking(String bookingDate, String userId, String restaurantPlaceId, String restaurantName) {
        Booking bookingToCreate = new Booking(bookingDate, userId, restaurantPlaceId, restaurantName);
        return WorkmateHelper.getWorkmatesCollection().add(bookingToCreate);
    }

    public static Task<Void> createLike(String restaurantId, String userId) {
        Map<String, Object> user = new HashMap<>();
        user.put(userId, true);
        return WorkmateHelper.getLikedCollection().document(restaurantId).set(user, SetOptions.merge());
    }

    // ----- GET -----

    public static FirebaseUser getCurrentWorkmate() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public static Task<DocumentSnapshot> getWorkmate(String id) {
        return WorkmateHelper.getWorkmatesCollection().document( id ).get();
    }

    public static Task<DocumentSnapshot> getWorkmatesForRestaurant(String placeId) {
        //TODO : filtrer les workmates et vérifier lequel mange à ce restaurant
        //return WorkmateHelper.getWorkmatesCollection().whereEqualTo( "restaurantId", placeId ).get();
        return WorkmateHelper.getWorkmatesCollection().document( placeId ).get();
    }


    public static Task<QuerySnapshot> getAllWorkmates() {
        return WorkmateHelper.getWorkmatesCollection().get();
    }

}
