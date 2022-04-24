package fr.steve.leroy.go4lunch.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import fr.steve.leroy.go4lunch.model.Booking;
import fr.steve.leroy.go4lunch.model.Like;

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

    private static final String TAG = "LikeHelper";

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

    public static Task<QuerySnapshot> getWorkmatesWhoHaveSameChoice(String placeId) {
        return getLikeCollection().whereEqualTo(PLACE_ID_FIELD, placeId).get();
    }

    public static Task<QuerySnapshot> getAllLikeByUserId(String uid, String placeId) {
        return LikeHelper.getLikeCollection().whereEqualTo( "uid", uid ).whereEqualTo( "placeId", placeId ).get();
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
    public static void deleteLike(String placeId, String uid) {
        FirebaseFirestore.getInstance().collection( COLLECTION_LIKED_RESTAURANT )
                .whereEqualTo( PLACE_ID_FIELD, placeId )
                .whereEqualTo( USER_ID_FIELD, uid )
                .get()
                .addOnCompleteListener( bookingTask -> {
                    if (bookingTask.isSuccessful()) {
                        for (QueryDocumentSnapshot restaurant : bookingTask.getResult()) {
                            FirebaseFirestore.getInstance().collection( COLLECTION_LIKED_RESTAURANT ).document( restaurant.getId() )
                                    .delete()
                                    .addOnSuccessListener( new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d( TAG, "DocumentSnapshot successfully deleted!" );
                                        }
                                    } )
                                    .addOnFailureListener( new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w( TAG, "Error deleting document", e );
                                        }
                                    } );
                        }
                    }
                } );
    }
}