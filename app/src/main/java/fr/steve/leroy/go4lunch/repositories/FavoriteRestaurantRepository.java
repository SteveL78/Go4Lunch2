package fr.steve.leroy.go4lunch.repositories;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import fr.steve.leroy.go4lunch.model.FavoriteRestaurant;
import fr.steve.leroy.go4lunch.model.User;

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


    @Nullable
    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    @Nullable
    public String getCurrentUserUID() {
        FirebaseUser user = getCurrentUser();
        return (user != null) ? user.getUid() : null;
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
