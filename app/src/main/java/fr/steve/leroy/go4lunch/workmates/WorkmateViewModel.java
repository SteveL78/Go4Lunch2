package fr.steve.leroy.go4lunch.workmates;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import fr.steve.leroy.go4lunch.firebase.WorkmateHelper;
import fr.steve.leroy.go4lunch.model.Workmate;
import fr.steve.leroy.go4lunch.repositories.WorkmateDataRepository;

/**
 * Created by Steve LEROY on 03/10/2021.
 */
public class WorkmateViewModel extends ViewModel {

    private MutableLiveData<List<Workmate>> workmates = new MutableLiveData<>();

    public LiveData<List<Workmate>> getWorkmates() {
        return workmates;
    }

    private String workmateId;

    public void init() {
        WorkmateDataRepository workmateRepository = WorkmateDataRepository.getInstance();
        workmateId = workmateRepository.getCurrentUserId();
    }

    public void getAllUsers() {
        WorkmateHelper.getAllWorkmates()
                .addOnSuccessListener( queryDocumentSnapshots -> {  //Callback
                    List<Workmate> workmateList = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        Workmate workmateFetched = documentSnapshot.toObject( Workmate.class );
                        if (!Objects.requireNonNull( workmateFetched ).getWorkmateId().equals( workmateId )) {
                            workmateList.add( workmateFetched );
                        }
                    }
                    this.workmates.setValue( workmateList );
                } );
    }

}
