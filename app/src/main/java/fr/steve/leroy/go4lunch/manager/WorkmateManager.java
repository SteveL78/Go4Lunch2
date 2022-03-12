package fr.steve.leroy.go4lunch.manager;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Steve LEROY on 07/03/2022.
 */
public class WorkmateManager {

    private static volatile WorkmateManager instance;
    private WorkmateManager userRepository;

    private WorkmateManager() {
        userRepository = WorkmateManager.getInstance();
    }

    public static WorkmateManager getInstance() {
        WorkmateManager result = instance;
        if (result != null) {
            return result;
        }
        synchronized (WorkmateManager.class) {
            if (instance == null) {
                instance = new WorkmateManager();
            }
            return instance;
        }
    }

    public FirebaseUser getCurrentWorkmate(){
        return userRepository.getCurrentWorkmate();
    }

    public Boolean isCurrentWorkmateLogged(){
        return (this.getCurrentWorkmate() != null);
    }

}
