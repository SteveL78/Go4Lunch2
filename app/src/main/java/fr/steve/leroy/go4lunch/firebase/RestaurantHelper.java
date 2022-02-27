package fr.steve.leroy.go4lunch.firebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import fr.steve.leroy.go4lunch.model.Booking;

/**
 * Created by Steve LEROY on 21/01/2022.
 */
public class RestaurantHelper {

    public static final String COLLECTION_BOOKING = "booking";
    private static final String COLLECTION_LIKED_NAME = "restaurantLiked";

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getBookingCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_BOOKING);
    }

    public static CollectionReference getLikedCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_LIKED_NAME);
    }


    // --- CREATE ---

    public static Task<DocumentReference> createBooking(String bookingDate, String workmateId, String placeId, String restaurantName) {
        Booking bookingToCreate = new Booking(bookingDate, workmateId, placeId, restaurantName);
        return RestaurantHelper.getBookingCollection().add(bookingToCreate);
    }

    public static Task<Void> createLike(String placeId, String workmateId) {
        Map<String, Object> like = new HashMap<>();
        like.put("workmateId", workmateId);
        like.put( "placeId", placeId );
        return RestaurantHelper.getLikedCollection().document(placeId + workmateId).set(like, SetOptions.merge());
    }

    // --- GET ---

    public static Task<QuerySnapshot> getBooking(String userId, String bookingDate) {
        return RestaurantHelper.getBookingCollection().whereEqualTo("workmateId", userId).whereEqualTo("bookingDate", bookingDate).get();
    }

    public static Task<QuerySnapshot> getTodayBooking(String placeId, String bookingDate) {
        return RestaurantHelper.getBookingCollection().whereEqualTo("placeId", placeId).whereEqualTo("bookingDate", bookingDate).get();
    }

    public static Task<DocumentSnapshot> getLikeForThisRestaurant(String placeId) {
        //TODO : changer la façon de récupérer le like
        return RestaurantHelper.getLikedCollection().document(placeId).get();
    }

    public static Task<QuerySnapshot> getAllLikeByUserId(String workmateId) {
        return RestaurantHelper.getLikedCollection().whereEqualTo(workmateId, true).get();
    }

    // --- DELETE ---

    public static Task<Void> deleteBooking(String bookingId) {
        return RestaurantHelper.getBookingCollection().document(bookingId).delete();
    }

    public static Boolean deleteLike(String placeId, String workmateId) {
        RestaurantHelper.getLikeForThisRestaurant(placeId).addOnCompleteListener(restaurantTask -> {
            if (restaurantTask.isSuccessful()) {
                Map<String, Object> update = new HashMap<>();
                update.put(workmateId, FieldValue.delete());
                RestaurantHelper.getLikedCollection().document(placeId).update(update);
            }
        });
        return true;
    }
}
