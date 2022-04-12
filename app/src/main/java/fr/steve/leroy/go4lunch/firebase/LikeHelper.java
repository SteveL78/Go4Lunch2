package fr.steve.leroy.go4lunch.firebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.steve.leroy.go4lunch.model.Booking;
import fr.steve.leroy.go4lunch.model.User;

/**
 * Created by Steve LEROY on 21/01/2022.
 */
public class LikeHelper {

    public static final String COLLECTION_LIKED_RESTAURANT = "likedRestaurant";
    private static final String USER_ID_FIELD = "uid";
    private static final String USERNAME_FIELD = "username";
    private static final String PLACE_ID_FIELD = "placeId";
    private static final String RESTAURANT_NAME_FIELD = "restaurantName";
    private static final String IS_LIKED_FIELD = "isLiked";


    // --- COLLECTION REFERENCE ---

    public static CollectionReference getLikeCollection() {
        return FirebaseFirestore.getInstance().collection( COLLECTION_LIKED_RESTAURANT );
    }



    // --- CREATE ---

    public static Task<Void> createLike(String placeId, String restaurantName, String uid, String username) {
        Map<String, Object> like = new HashMap<>();
        like.put( PLACE_ID_FIELD, placeId );
        like.put( RESTAURANT_NAME_FIELD, restaurantName );
        like.put( USER_ID_FIELD, uid );
        like.put( USERNAME_FIELD, username );
        return LikeHelper.getLikeCollection().document( placeId + restaurantName + uid + username ).set( like, SetOptions.merge() );
    }


    // --- GET ---

    public static Task<DocumentSnapshot> getLikeForThisRestaurant(String placeId) {
        //TODO : changer la façon de récupérer le like
        return LikeHelper.getLikeCollection().document( placeId ).get();
    }

    public static Task<QuerySnapshot> getAllLikeByUserId(String uid) {
        return LikeHelper.getLikeCollection().whereEqualTo( uid, true ).get();
    }



    // --- UPDATE ---

    public static Task<Void> updateLikeList(String uid, String username, String placeId, String restaurantName) {
        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put( USERNAME_FIELD, username );
        updatedData.put( PLACE_ID_FIELD, placeId );
        updatedData.put( RESTAURANT_NAME_FIELD, restaurantName );
        return LikeHelper.getLikeCollection().document( uid ).update( updatedData );
    }


    // --- DELETE ---

    // TODO : delete pas le document mais la collection
    public static Boolean deleteLike(String placeId, String uid) {
        LikeHelper.getLikeForThisRestaurant( placeId ).addOnCompleteListener( restaurantTask -> {
            if (restaurantTask.isSuccessful()) {
                Map<String, Object> update = new HashMap<>();
                update.put( uid, FieldValue.delete() );
                LikeHelper.getLikeCollection().document( placeId ).update( update );
            }
        } );
        return true;
    }


}