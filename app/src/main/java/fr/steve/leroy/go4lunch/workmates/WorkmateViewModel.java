package fr.steve.leroy.go4lunch.workmates;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import fr.steve.leroy.go4lunch.firebase.UserHelper;
import fr.steve.leroy.go4lunch.model.User;

/**
 * Created by Steve LEROY on 03/10/2021.
 */
public class WorkmateViewModel extends ViewModel {

    private MutableLiveData<List<User>> users = new MutableLiveData<>();

    public LiveData<List<User>> getUsers() {
        return users;
    }

    private String userId;

    public void init() {
        UserDataRepository userRepository = UserDataRepository.getInstance();
        userId = userRepository.getCurrentUserId();
    }

    public void getAllUsers() {
        UserHelper.getAllUser()
                .addOnSuccessListener( queryDocumentSnapshots -> {
                    List<User> usersList = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        User userFetched = documentSnapshot.toObject( User.class );
                        if (!Objects.requireNonNull( userFetched ).getUserId().equals( userId )) {
                            usersList.add( userFetched );
                        }
                    }
                    users.setValue( usersList );
                } );
    }

}
