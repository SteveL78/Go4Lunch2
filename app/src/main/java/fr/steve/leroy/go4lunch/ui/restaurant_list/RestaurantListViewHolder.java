package fr.steve.leroy.go4lunch.ui.restaurant_list;

import android.content.Context;
import android.content.res.Resources;
import android.location.Location;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.widget.TextViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.maps.model.PlacesSearchResult;

import java.util.Objects;

import fr.steve.leroy.go4lunch.R;
import fr.steve.leroy.go4lunch.databinding.RestaurantItemBinding;
import fr.steve.leroy.go4lunch.manager.UserManager;
import fr.steve.leroy.go4lunch.repositories.UserRepository;

/**
 * Created by Steve LEROY on 02/10/2021.
 */
public class RestaurantListViewHolder extends RecyclerView.ViewHolder {

    private UserManager userManager = UserManager.getInstance();

    private RestaurantItemBinding binding;
    private Context context;
    private Resources resources;
    private int numberWorkmateEatingHere = 0;
    public static final double MAX_STAR = 3;
    public static final double MAX_RATING = 5;


    public RestaurantListViewHolder(@NonNull RestaurantItemBinding binding, Context context) {
        super( binding.getRoot() );
        this.binding = binding;
        this.context = context;
        resources = binding.getRoot().getResources();

    }


    public void updateRestaurantInfo(PlacesSearchResult placeSearchResults, android.location.Location currentLocation) {
        binding.restaurantNameTv.setText( placeSearchResults.name );
        binding.restaurantAddressTv.setText( placeSearchResults.vicinity );

        displayOpeningHours( placeSearchResults );

        displayRestaurantRating( placeSearchResults );

        displayRestaurantDistance( placeSearchResults, currentLocation );

        displayRestaurantPhoto( placeSearchResults );

        updateWorkmateNumber( placeSearchResults );

    }


    private void updateWorkmateNumber(PlacesSearchResult placesSearchResults) {

        UserRepository.getUsersCollection()
                .whereEqualTo( "placeId", placesSearchResults.placeId )
                .get()
                .addOnCompleteListener( task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull( task.getResult() )) {
                            numberWorkmateEatingHere++;
                        }
                        if (numberWorkmateEatingHere > 0) {
                            String numberOfUsers = "(" + numberWorkmateEatingHere + ")";
                            binding.itemRestaurantListNumberWorkmatesTv.setText( numberOfUsers );
                        } else {
                            this.binding.personIcon.setVisibility( View.INVISIBLE );
                        }
                    } else {
                        Log.d( "updateWorkmateNumber", "Error getting documents: ", task.getException() );
                    }
                } );
    }


    private void displayRestaurantPhoto(PlacesSearchResult placesSearchResults) {

        if (placesSearchResults.photos != null && placesSearchResults.photos.length > 0) {

            Log.d( "photos", placesSearchResults.photos[0].toString() );

            String urlPicture = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
                    + placesSearchResults.photos[0].photoReference
                    + "&key="
                    + context.getString( R.string.google_maps_API_key );

            Glide.with( context )
                    .load( urlPicture )
                    .centerCrop()
                    .into( binding.restaurantImg );
        } else {
            binding.restaurantImg.setImageResource( R.drawable.image_not_avalaible );
        }
    }


    private void displayRestaurantDistance(PlacesSearchResult placesSearchResults, Location currentLocation) {

        double userLocationLat = currentLocation.getLatitude();
        double userLocationLng = currentLocation.getLongitude();

        Location userLocation = new Location( "Starting point" );
        userLocation.setLatitude( userLocationLat );
        userLocation.setLongitude( userLocationLng );

        Location selectedRestaurantLocation = new Location( "Arrival point" );
        selectedRestaurantLocation.setLatitude( placesSearchResults.geometry.location.lat );
        selectedRestaurantLocation.setLongitude( placesSearchResults.geometry.location.lng );

        String distanceResult = String.valueOf( Math.round( userLocation.distanceTo( selectedRestaurantLocation ) ) );
        distanceResult = String.format( "%sm", distanceResult );

        binding.distanceRestaurantTv.setText( distanceResult );
    }

    private void displayRestaurantRating(PlacesSearchResult placesSearchResults) {
        double googleRating = placesSearchResults.rating;
        double rating = (googleRating / MAX_RATING) * MAX_STAR;

        binding.itemRestaurantListRatingbar.setRating( (float) rating );
        binding.itemRestaurantListRatingbar.setVisibility( View.VISIBLE );

    }


    private void displayOpeningHours(PlacesSearchResult placesSearchResults) {
        boolean isOpen = false;
        if (placesSearchResults.openingHours != null) {
            isOpen = Boolean.parseBoolean( placesSearchResults.openingHours.openNow.toString() );
        }
        boolean isPermanentlyClosed = Boolean.parseBoolean( String.valueOf( placesSearchResults.permanentlyClosed ) );

        if (isPermanentlyClosed) {
            binding.openingTimeTv.setText( "Permanently closed" );
            TextViewCompat.setTextAppearance( binding.openingTimeTv, R.style.closedRestaurant );
        } else if (isOpen) {
            binding.openingTimeTv.setText( R.string.open_restaurant );
        } else {
            binding.openingTimeTv.setText( R.string.closed_restaurant );
            TextViewCompat.setTextAppearance( binding.openingTimeTv, R.style.closedRestaurant );
        }

    }

}