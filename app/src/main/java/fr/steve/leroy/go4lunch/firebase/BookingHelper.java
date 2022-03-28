package fr.steve.leroy.go4lunch.firebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import fr.steve.leroy.go4lunch.model.Booking;

/**
 * Created by Steve LEROY on 28/03/2022.
 */
public class BookingHelper {

    public static final String COLLECTION_BOOKING = "bookingRestaurant";
    private static final String USER_ID_FIELD = "uid";
    private static final String USERNAME_FIELD = "username";
    private static final String PLACE_ID_FIELD = "placeId";
    private static final String RESTAURANT_NAME_FIELD = "restaurantName";
    private static final String HAS_BOOKED_FIELD = "hasBooked";

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getBookingCollection() {
        return FirebaseFirestore.getInstance().collection( COLLECTION_BOOKING );
    }

    // Create booking
    public static Task<Void> createBooking(String uid,String username, String placeId, String restaurantName) {
        Booking bookingToCreate = new Booking( uid, username, placeId, restaurantName );
        return BookingHelper.getBookingCollection().document( uid ).set( bookingToCreate );
    }


    // --- GET ---
    public static Task<QuerySnapshot> getBooking(String userId) {
        return BookingHelper.getBookingCollection().whereEqualTo( "workmateId", userId ).get();
    }

    // --- UPDATE ---
    public static Task<Void> updateBooking(String uid) {
        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put( "placeId", true );
        updatedData.put( "restaurantName", true );
        return BookingHelper.getBookingCollection().document( uid ).update( updatedData );
    }


}
