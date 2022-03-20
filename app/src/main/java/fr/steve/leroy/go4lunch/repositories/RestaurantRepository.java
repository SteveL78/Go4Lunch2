package fr.steve.leroy.go4lunch.repositories;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Steve LEROY on 01/10/2021.
 */
public class RestaurantRepository {

    private static final String COLLECTION_NAME = "restaurants";
    private static final String PLACE_ID_FIELD = "placeId";
    private static final String RESTAURANT_NAME_FIELD = "restaurantName";
    private static final String URL_PICTURE_FIELD = "urlPicture";
    private static final String HAS_BOOKED_FIELD = "hasBooked";

    private static RestaurantRepository instance;

    public static RestaurantRepository getInstance() {
        RestaurantRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized (RestaurantRepository.class) {
            if (instance == null) {
                instance = new RestaurantRepository();
            }
            return instance;
        }
    }

    public String getCurrentUserId() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseUser workmate = FirebaseAuth.getInstance().getCurrentUser();
            return workmate.getUid();
        }
        return null;
    }


}
