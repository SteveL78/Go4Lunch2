package fr.steve.leroy.go4lunch.ui.restaurant_details;

import androidx.lifecycle.ViewModel;

/**
 * Created by Steve LEROY on 05/12/2021.
 */
public class RestaurantDetailViewModel extends ViewModel {
    /*
        private MutableLiveData<List<Workmate>> workmates = new MutableLiveData<>();

        public LiveData<List<Workmate>> getWorkmates() {
            return workmates;
        }

        private String uid;
        private String placeId;

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
    */
    public void getWorkmatesForRestaurant() {
      /*
        WorkmateHelper.getWorkmatesForRestaurant(restaurantId)
                .addOnSuccessListener( queryDocumentSnapshots -> {  //Callback
                    List<Workmate> workmateList = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        Workmate workmateFetched = documentSnapshot.toObject( Workmate.class );
                        if (!Objects.requireNonNull( workmateFetched ).getPlaceId().equals( placeId )) {
                            workmateList.add( workmateFetched );
                        }
                    }
                    this.workmates.setValue( workmateList );
                } );*/
    }

}
