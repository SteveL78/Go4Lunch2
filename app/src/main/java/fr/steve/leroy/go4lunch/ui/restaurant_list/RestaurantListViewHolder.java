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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.maps.model.PlacesSearchResult;

import java.util.Objects;

import fr.steve.leroy.go4lunch.R;
import fr.steve.leroy.go4lunch.databinding.RestaurantItemBinding;
import fr.steve.leroy.go4lunch.firebase.WorkmateHelper;

/**
 * Created by Steve LEROY on 02/10/2021.
 */
public class RestaurantListViewHolder extends RecyclerView.ViewHolder {

    private RestaurantItemBinding binding;
    private Context context;
    private Resources resources;
    private FirebaseAuth mAuth;
    private int numberWorkmateEatingHere = 0;


    public RestaurantListViewHolder(@NonNull RestaurantItemBinding binding, Context context) {
        super( binding.getRoot() );
        this.binding = binding;
        this.context = context;
        resources = binding.getRoot().getResources();

    }

    public void updateRestaurantInfo(PlacesSearchResult placesSearchResult, Location currentLocation) {
        binding.restaurantNameTv.setText( placesSearchResult.name );
        binding.restaurantAddressTv.setText( placesSearchResult.vicinity );

        displayOpeningHours( placesSearchResult );

        restaurantRating( placesSearchResult );

        restaurantDistance( placesSearchResult, currentLocation );

        displayRestaurantPhoto( placesSearchResult );

        updateWorkmateNumber( placesSearchResult );

    }


    private void updateWorkmateNumber(PlacesSearchResult placesSearchResult) {

        WorkmateHelper.getWorkmatesCollection()
                .whereEqualTo( "restaurantId", placesSearchResult.placeId )
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


    private void displayRestaurantPhoto(PlacesSearchResult placesSearchResult) {

        if (placesSearchResult.photos != null && placesSearchResult.photos.length > 0) {

            Log.d( "photos", placesSearchResult.photos[0].toString() );

            String imageurl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
                    + placesSearchResult.photos[0].photoReference
                    + "&key="
                    + context.getString( R.string.google_maps_API_key );

            Glide.with( context )
                    .load( imageurl )
                    .centerCrop()
                    .into( binding.restaurantImg );
        } else {
            binding.restaurantImg.setImageResource( R.drawable.image_not_avalaible );
        }
    }


    private void restaurantDistance(PlacesSearchResult placesSearchResult, Location currentLocation) {

        double userLocationLat = currentLocation.getLatitude();
        double userLocationLng = currentLocation.getLongitude();

        Location userLocation = new Location( "Starting point" );
        userLocation.setLatitude( userLocationLat );
        userLocation.setLongitude( userLocationLng );

        Location selectedRestaurantLocation = new Location( "Arrival point" );
        selectedRestaurantLocation.setLatitude( placesSearchResult.geometry.location.lat );
        selectedRestaurantLocation.setLongitude( placesSearchResult.geometry.location.lng );

        String distanceResult = String.valueOf( Math.round( userLocation.distanceTo( selectedRestaurantLocation ) ) );
        distanceResult = String.format( "%sm", distanceResult );

        binding.distanceRestaurantTv.setText( distanceResult );
    }

    private void restaurantRating(PlacesSearchResult placesSearchResult) {
        float restaurantRating = placesSearchResult.rating;
        float rating = (restaurantRating / 5) * 3;

        binding.itemRestaurantListRatingbar.setRating( rating );
        binding.itemRestaurantListRatingbar.setVisibility( View.VISIBLE );

    }


    private void displayOpeningHours(PlacesSearchResult placesSearchResult) {
        boolean isOpen = Boolean.parseBoolean( placesSearchResult.openingHours.openNow.toString() );
        boolean isPermanentlyClosed = Boolean.parseBoolean( String.valueOf( placesSearchResult.permanentlyClosed ) );

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