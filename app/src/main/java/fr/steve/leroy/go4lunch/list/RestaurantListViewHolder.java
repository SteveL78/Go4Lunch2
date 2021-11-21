package fr.steve.leroy.go4lunch.list;

import android.content.Context;
import android.content.res.Resources;
import android.location.Location;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.maps.model.PlacesSearchResult;

import fr.steve.leroy.go4lunch.R;
import fr.steve.leroy.go4lunch.databinding.RestaurantItemBinding;

/**
 * Created by Steve LEROY on 02/10/2021.
 */
public class RestaurantListViewHolder extends RecyclerView.ViewHolder {

    private RestaurantItemBinding binding;
    private Context context;
    private Resources resources;

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

    }

    private void displayRestaurantPhoto(PlacesSearchResult placesSearchResult) {

        if (placesSearchResult.photos != null && placesSearchResult.photos.length > 0) {

            Log.d( "photos", placesSearchResult.photos[0].toString() );

            String imageurl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + placesSearchResult.photos[0].photoReference + "&key=" + context.getString( R.string.google_maps_API_key );

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

            //TODO : afficher la notion de metre
            String distanceResult = String.valueOf( Math.round( userLocation.distanceTo( selectedRestaurantLocation ) ));
            distanceResult = String.format( "%sm", distanceResult );

            binding.distanceRestaurantTv.setText( distanceResult);
    }

    private void restaurantRating(PlacesSearchResult placesSearchResult) {
        float restaurantRating = placesSearchResult.rating;
        float rating = (restaurantRating / 5) * 3;

        if (rating > 1 && rating < 2) {
            binding.itemRestaurantListRatingbar.setRating( (float) rating );
            binding.itemRestaurantListRatingbar.setVisibility( View.VISIBLE );
        } else if (rating > 2 && rating < 3) {
            binding.itemRestaurantListRatingbar.setRating( (float) rating );
            binding.itemRestaurantListRatingbar.setVisibility( View.VISIBLE );
        } else {
            binding.itemRestaurantListRatingbar.setRating( (float) rating );
            binding.itemRestaurantListRatingbar.setVisibility( View.VISIBLE );
            // binding.itemRestaurantListRatingbar.setVisibility( View.GONE );
        }
    }


    private void displayOpeningHours(PlacesSearchResult placesSearchResult) {
        boolean result = Boolean.parseBoolean( placesSearchResult.openingHours.openNow.toString() );
        if (result) {
            binding.openingTimeTv.setText( "Open" );
        } else {
            binding.openingTimeTv.setText( "Closed" );
        }

        /*
        boolean result, result2;
        result = Boolean.parseBoolean( placesSearchResult.openingHours.openNow.toString() );
        result2 = Boolean.parseBoolean( placesSearchResult.openingHours.permanentlyClosed.toString() );

        switch (result + "-" + result2) {
            case "false-false":
                binding.openingTimeTv.setText( "Closed" );
            case "false-true":
                binding.openingTimeTv.setText( "Permanently closed" );
            case "true-false":
                binding.openingTimeTv.setText( "Open" );
            case "true-true":
                binding.openingTimeTv.setText( "Please call the restaurant" );
            default:
                throw new RuntimeException(
                        "something strange happening here, open: " + result + ",closed: " + result2);
        }
         */

    }

}