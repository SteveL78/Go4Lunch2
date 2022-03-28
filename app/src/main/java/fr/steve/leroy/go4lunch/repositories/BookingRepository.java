package fr.steve.leroy.go4lunch.repositories;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import fr.steve.leroy.go4lunch.firebase.RestaurantHelper;
import fr.steve.leroy.go4lunch.model.Booking;

/**
 * Created by Steve LEROY on 20/03/2022.
 */
public class BookingRepository {

    private static final String COLLECTION_NAME = "booking";
    private static final String USER_ID_FIELD = "uid";
    private static final String USERNAME_FIELD = "username";
    private static final String PLACE_ID_FIELD = "placeId";
    private static final String RESTAURANT_NAME_FIELD = "restaurantName";
    private static final String DATE_CREATED_FIELD = "dateCreated";
    private static final String HAS_BOOKED_FIELD = "hasBooked";

    private static volatile BookingRepository instance;

    private BookingRepository() {
    }


    // --- COLLECTION REFERENCE ---
    public static CollectionReference getBookingCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }


    // --- CREATE ---
    public static Task<Void> createBooking(String uid, String username, String placeId, String restaurantName) {
        Booking bookingToCreate = new Booking(uid, username, placeId, restaurantName);
        return RestaurantHelper.getBookingCollection().document(uid).set(bookingToCreate);
    }


    // --- GET ---
    public static Task<QuerySnapshot> getBooking(String uid, Date date) {
        return RestaurantHelper.getBookingCollection().whereEqualTo(USER_ID_FIELD, uid).whereEqualTo(DATE_CREATED_FIELD, date).get();
    }

    public static Task<QuerySnapshot> getTodayBooking(String placeId, String bookingDate) {
        return RestaurantHelper.getBookingCollection().whereEqualTo(PLACE_ID_FIELD, placeId).whereEqualTo(DATE_CREATED_FIELD, bookingDate).get();
    }


    // --- UPDATE ---
    public static Task<Void> updateBooking(String uid, String restaurantName, String placeId) {
        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put(RESTAURANT_NAME_FIELD, restaurantName);
        updatedData.put( PLACE_ID_FIELD, placeId );
        return RestaurantHelper.getBookingCollection().document(uid).update(updatedData);
    }


    // --- DELETE ---
    public static Task<Void> deleteBooking(String bookingId) {
        return RestaurantHelper.getBookingCollection().document(bookingId).delete();
    }

}
