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

    public static Task<DocumentReference> createBooking(String bookingDate, String userId, String restaurantPlaceId, String restaurantName) {
        Booking bookingToCreate = new Booking(bookingDate, userId, restaurantPlaceId, restaurantName);
        return RestaurantHelper.getBookingCollection().add(bookingToCreate);
    }

    public static Task<Void> createLike(String restaurantId, String userId) {
        Map<String, Object> user = new HashMap<>();
        user.put(userId, true);
        return RestaurantHelper.getLikedCollection().document(restaurantId).set(user, SetOptions.merge());
    }

    // --- GET ---

    public static Task<QuerySnapshot> getBooking(String userId, String bookingDate) {
        return RestaurantHelper.getBookingCollection().whereEqualTo("workmateId", userId).whereEqualTo("bookingDate", bookingDate).get();
    }

    public static Task<QuerySnapshot> getTodayBooking(String restaurantPlaceId, String bookingDate) {
        return RestaurantHelper.getBookingCollection().whereEqualTo("restaurantId", restaurantPlaceId).whereEqualTo("bookingDate", bookingDate).get();
    }

    public static Task<DocumentSnapshot> getLikeForThisRestaurant(String restaurantPlaceId) {
        return RestaurantHelper.getLikedCollection().document(restaurantPlaceId).get();
    }

    public static Task<QuerySnapshot> getAllLikeByUserId(String userId) {
        return RestaurantHelper.getLikedCollection().whereEqualTo(userId, true).get();
    }

    // --- DELETE ---

    public static Task<Void> deleteBooking(String bookingId) {
        return RestaurantHelper.getBookingCollection().document(bookingId).delete();
    }

}
