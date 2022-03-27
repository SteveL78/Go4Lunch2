package fr.steve.leroy.go4lunch.ui.workmates_list;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import fr.steve.leroy.go4lunch.manager.UserManager;
import fr.steve.leroy.go4lunch.model.User;
import fr.steve.leroy.go4lunch.repositories.UserRepository;

/**
 * Created by Steve LEROY on 03/10/2021.
 */
public class WorkmateViewModel extends ViewModel {

    private UserManager userManager = UserManager.getInstance();

    private MutableLiveData<List<User>> userList = new MutableLiveData<>();

    public LiveData<List<User>> getWorkmateList() {
        return userList;
    }

    private String workmateId;


    public void init() {
        UserRepository userRepository = UserRepository.getInstance();
        workmateId = userRepository.getCurrentUserUID();
    }

    public void getAllUsers() {
        userManager.getAllUsers()
                .addOnSuccessListener( queryDocumentSnapshots -> {  //Callback
                    List<User> workmatesList = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        User workmateFetched = documentSnapshot.toObject( User.class );
                        if (!Objects.requireNonNull( workmateFetched ).getUid().equals( workmateId )) {
                            workmatesList.add( workmateFetched );
                        }
                    }
                    this.userList.setValue( workmatesList );
                } );
    }

}
