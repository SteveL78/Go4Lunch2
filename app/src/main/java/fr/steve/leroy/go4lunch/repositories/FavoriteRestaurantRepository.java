package fr.steve.leroy.go4lunch.repositories;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Created by Steve LEROY on 20/03/2022.
 */
public class FavoriteRestaurantRepository {

    private static final String COLLECTION_NAME = "favoritesRestaurants";
    private static final String USER_ID_FIELD = "uid";
    private static final String USERNAME_FIELD = "username";
    private static final String PLACE_ID_FIELD = "placeId";
    private static final String RESTAURANT_NAME_FIELD = "restaurantName";


    private static volatile FavoriteRestaurantRepository instance;

    private FavoriteRestaurantRepository() {
    }

    public static FavoriteRestaurantRepository getInstance() {
        FavoriteRestaurantRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized (FavoriteRestaurantRepository.class) {
            if (instance == null) {
                instance = new FavoriteRestaurantRepository();
            }
            return instance;
        }
    }


    /*********************
     *     FIRESTORE     *
     ********************/

    // Get the Collection Reference
    private CollectionReference getFavoritesRestaurantsCollection() {
        return FirebaseFirestore.getInstance().collection( COLLECTION_NAME );
    }

    // Create a Favorite in Firestore
    public void createFavorite() {

    }

}
