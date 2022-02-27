package fr.steve.leroy.go4lunch.firebase;


import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import fr.steve.leroy.go4lunch.model.Workmate;

/**
 * Created by Steve LEROY on 03/10/2021.
 */
public class WorkmateHelper {

    private static final String COLLECTION_NAME = "workmate";

    // ----- COLLECTION REFERENCE -----
    public static CollectionReference getWorkmatesCollection() {
        return FirebaseFirestore.getInstance().collection( COLLECTION_NAME );
    }

    // --- CREATE ---
    public static Task<Void> createWorkmate(String workmateId, @Nullable String profileUrl, String firstName) {
        if (profileUrl == null)
            profileUrl = "https://unc.nc/wp-content/uploads/2020/07/Portrait_Placeholder-1.png";
        Workmate workmateToCreate = new Workmate( workmateId, profileUrl, firstName );
        return WorkmateHelper.getWorkmatesCollection().document( workmateId ).set( workmateToCreate );
    }


    // ----- GET -----

    public static FirebaseUser getCurrentWorkmate() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public static Task<DocumentSnapshot> getWorkmate(String workmateId) {
        return WorkmateHelper.getWorkmatesCollection().document( workmateId ).get();
    }

    public static Task<DocumentSnapshot> getWorkmatesForRestaurant(String placeId) {
        //TODO : filtrer les workmates et vérifier lequel mange à ce restaurant
        //return WorkmateHelper.getWorkmatesCollection().whereEqualTo( "restaurantId", placeId ).get();
        return WorkmateHelper.getWorkmatesCollection().document( placeId ).get();
    }

    public static Task<QuerySnapshot> getAllWorkmates() {
        return WorkmateHelper.getWorkmatesCollection().get();
    }

}
