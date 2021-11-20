package fr.steve.leroy.go4lunch.list;

import android.content.Context;
import android.content.res.Resources;

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



    }


    private void displayOpeningHours(PlacesSearchResult placesSearchResult) {
        boolean result = Boolean.parseBoolean( placesSearchResult.openingHours.openNow.toString() );
        if (result) {
            binding.openingTimeTv.setText( "Open" );
        } else {
            binding.openingTimeTv.setText( "Closed" );
        }

    }

}