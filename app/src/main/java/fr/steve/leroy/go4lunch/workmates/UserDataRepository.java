package fr.steve.leroy.go4lunch.workmates;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

/**
 * Created by Steve LEROY on 01/10/2021.
 */
public class UserDataRepository {

    private static UserDataRepository sUserRepository;

    public static UserDataRepository getInstance() {
        if (sUserRepository == null){
            sUserRepository = new UserDataRepository();
        }
        return sUserRepository;
    }

    public String getCurrentUserId() {
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            return user.getUid();
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
