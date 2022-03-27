package fr.steve.leroy.go4lunch.repositories;

import android.content.Context;

import androidx.annotation.Nullable;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import fr.steve.leroy.go4lunch.model.User;

/**
 * Created by Steve LEROY on 07/03/2022.
 */
public class UserRepository {

    private static final String COLLECTION_NAME = "users";
    private static final String USER_ID_FIELD = "uid";
    private static final String USERNAME_FIELD = "username";
    private static final String PLACE_ID_FIELD = "placeId";
    private static final String URL_PICTURE_FIELD = "urlPicture";
    private static final String RESTAURANT_NAME_FIELD = "restaurantName";
    private static final String HAS_BOOKED_FIELD = "hasBooked";


    private static volatile UserRepository instance;

    private UserRepository() {
    }

    public static UserRepository getInstance() {
        UserRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized (UserRepository.class) {
            if (instance == null) {
                instance = new UserRepository();
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


    public Task<Void> signOut(Context context) {
        return AuthUI.getInstance().signOut( context );
    }

    public Task<Void> deleteUser(Context context) {
        return AuthUI.getInstance().delete( context );
    }


    /*********************
     *     FIRESTORE     *
     ********************/

    // Get the Collection Reference
    public static CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection( COLLECTION_NAME );
    }

    // Create User in Firestore
    public void createUser() {
        FirebaseUser user = getCurrentUser();
        String defaultUrlPicture = "https://unc.nc/wp-content/uploads/2020/07/Portrait_Placeholder-1.png";
        if (user != null) {
            String uid = user.getUid();
            String username = user.getDisplayName();
            String urlPicture = (user.getPhotoUrl() != null) ? user.getPhotoUrl().toString() : defaultUrlPicture;
            String placeId = "";
            String restaurantName = "";
            List<String> likedRestaurantList = new ArrayList<>();

            User userToCreate = new User( uid, username, urlPicture, placeId, restaurantName, likedRestaurantList );

            Task<DocumentSnapshot> userData = getUserData();
            // If the user already exist in Firestore, we get his data (hasBooked)
            userData.addOnSuccessListener( documentSnapshot -> {
                if (documentSnapshot.contains( HAS_BOOKED_FIELD )) {
                    userToCreate.setHasBooked( (Boolean) documentSnapshot.get( HAS_BOOKED_FIELD ) );
                }
                this.getUsersCollection().document( uid ).set( userToCreate );
            } );
        }
    }

    // Get User Data from Firestore
    public Task<DocumentSnapshot> getUserData() {
        String uid = this.getCurrentUserUID();
        if (uid != null) {
            return this.getUsersCollection().document( uid ).get();
        } else {
            return null;
        }
    }

    // Get all Users
    public Task<QuerySnapshot> getAllUsers() {
        return UserRepository.getUsersCollection().get();
    }


    public static Task<DocumentSnapshot> getUser(String uid) {
        return UserRepository.getUsersCollection().document( uid ).get();
    }

    // Get Users for each restaurant (placeId)
    public static Task<DocumentSnapshot> getWorkmatesForRestaurant(String PLACE_ID_FIELD) {
        //TODO : filtrer les workmates et vérifier lequel mange à ce restaurant
        //return WorkmateHelper.getWorkmatesCollection().whereEqualTo( "restaurantId", placeId ).get();
        return UserRepository.getUsersCollection().document( PLACE_ID_FIELD ).get();
    }

    // Update User Username
    public Task<Void> updateUsername(String username) {
        String uid = this.getCurrentUserUID();
        if (uid != null) {
            return this.getUsersCollection().document( uid ).update( USERNAME_FIELD, username );
        } else {
            return null;
        }
    }

    // Update User hasBooked
    public void updateBooking(Boolean hasBooked) {
        String uid = this.getCurrentUserUID();
        if (uid != null) {
            this.getUsersCollection().document( uid ).update( HAS_BOOKED_FIELD, hasBooked );
        }
    }

    // Delete the User from Firestore
    public void deleteUserFromFirestore() {
        String uid = this.getCurrentUserUID();
        if (uid != null) {
            this.getUsersCollection().document( uid ).delete();
        }
    }

}
