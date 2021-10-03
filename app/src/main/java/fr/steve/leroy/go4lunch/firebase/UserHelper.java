package fr.steve.leroy.go4lunch.firebase;


import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * Created by Steve LEROY on 03/10/2021.
 */
public class UserHelper {

    private static final String COLLECTION_NAME = "allUsers";


    // ----- COLLECTION REFERENCE -----
    public static CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection( COLLECTION_NAME );
    }

    // ----- GET -----
    public static Task<QuerySnapshot> getAllUser() {
        return UserHelper.getUsersCollection().orderBy( "userName" ).get();
    }

}
