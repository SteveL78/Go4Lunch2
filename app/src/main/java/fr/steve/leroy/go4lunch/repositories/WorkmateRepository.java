package fr.steve.leroy.go4lunch.repositories;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Steve LEROY on 07/03/2022.
 */
public class WorkmateRepository {

    private static volatile WorkmateRepository instance;

    private WorkmateRepository() {  }

    public static WorkmateRepository getInstance() {
        WorkmateRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized (WorkmateRepository.class) {
            if (instance == null) {
                instance = new WorkmateRepository();
            }
            return instance;
        }
    }

    @Nullable
    public FirebaseUser getCurrentWorkmate(){
        return FirebaseAuth.getInstance().getCurrentUser();
    }

}
