package fr.steve.leroy.go4lunch.list;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.maps.model.PlacesSearchResult;

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

    public void updateRestaurantInfo(PlacesSearchResult placesSearchResult) {
        binding.restaurantNameTv.setText( placesSearchResult.name );
        binding.restaurantAddressTv.setText( placesSearchResult.vicinity );

        displayOpeningHours( placesSearchResult );

        restaurantRating( placesSearchResult );


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