package fr.steve.leroy.go4lunch.firebase;


import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * Created by Steve LEROY on 03/10/2021.
 */
public class WorkmateHelper {

    private static final String COLLECTION_NAME = "workmate";


    // ----- COLLECTION REFERENCE -----
    public static CollectionReference getWorkmatesCollection() {
        return FirebaseFirestore.getInstance().collection( COLLECTION_NAME );
    }

    // ----- GET -----

    public static Task<DocumentSnapshot> getWorkmate(String id){
        return WorkmateHelper.getWorkmatesCollection().document(id).get();
    }

    public static Task<DocumentSnapshot> getWorkmatesForRestaurant(String placeId){
        //TODO : filtrer les workmates et vérifier lequel mange à ce restaurant
        //return WorkmateHelper.getWorkmatesCollection().whereEqualTo( "restaurantId", placeId ).get();
       return WorkmateHelper.getWorkmatesCollection().document(placeId).get();
    }


    public static Task<QuerySnapshot> getAllWorkmates() {
        return WorkmateHelper.getWorkmatesCollection().get();
    }

}
