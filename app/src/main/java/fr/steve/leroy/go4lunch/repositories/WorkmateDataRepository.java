package fr.steve.leroy.go4lunch.repositories;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

/**
 * Created by Steve LEROY on 01/10/2021.
 */
public class WorkmateDataRepository {

    private static WorkmateDataRepository newInstance;

    public static WorkmateDataRepository getInstance(){
        if(newInstance == null){
            newInstance = new WorkmateDataRepository();
        }
        return newInstance;
    }

    public String getCurrentUserId() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseUser workmate = FirebaseAuth.getInstance().getCurrentUser();
            return workmate.getUid();
        }
        return null;
    }

    public void logOut() {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();

    }

    public void deleteUserFromFirebase() {
        Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).delete();
    }

}
