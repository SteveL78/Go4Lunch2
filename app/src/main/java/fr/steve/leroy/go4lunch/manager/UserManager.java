package fr.steve.leroy.go4lunch.manager;

import android.content.Context;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QuerySnapshot;

import fr.steve.leroy.go4lunch.model.User;
import fr.steve.leroy.go4lunch.repositories.UserRepository;

/**
 * Created by Steve LEROY on 07/03/2022.
 */
public class UserManager {

    private static volatile UserManager instance;
    private UserRepository userRepository;

    private UserManager() {
        userRepository = UserRepository.getInstance();
    }

    public static UserManager getInstance() {
        UserManager result = instance;
        if (result != null) {
            return result;
        }
        synchronized (UserRepository.class) {
            if (instance == null) {
                instance = new UserManager();
            }
            return instance;
        }
    }

    public FirebaseUser getCurrentUser() {
        return userRepository.getCurrentUser();
    }

    public Task<QuerySnapshot> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public Boolean isCurrentUserLogged() {
        return (this.getCurrentUser() != null);
    }

    public Task<Void> signOut(Context context) {
        return userRepository.signOut( context );
    }


    public void createUser() {
        userRepository.createUser();
    }

    public Task<User> getUserData() {
        // Get the user from Firestore and cast it to a User model Object
        return userRepository.getUserData().continueWith( task -> task.getResult().toObject( User.class ) );
    }

    public Task<Void> updateUsername(String username) {
        return userRepository.updateUsername( username );
    }

    public void updateHasBooked(Boolean hasBooked) {
        userRepository.updateBooking( hasBooked );
    }

    public Task<Void> deleteUser(Context context) {
        // Delete the user account from the Auth
        return userRepository.deleteUser( context ).addOnCompleteListener( task -> {
            // Once done, delete the user datas from Firestore
            userRepository.deleteUserFromFirestore();
        } );
    }

}
