package fr.steve.leroy.go4lunch.list;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.maps.model.PlacesSearchResult;

import java.util.Objects;

import fr.steve.leroy.go4lunch.R;
import fr.steve.leroy.go4lunch.databinding.RestaurantItemBinding;
import fr.steve.leroy.go4lunch.databinding.WorkmatesItemBinding;
import fr.steve.leroy.go4lunch.model.Restaurant;
import fr.steve.leroy.go4lunch.model.Workmate;

/**
 * Created by Steve LEROY on 02/10/2021.
 */
public class RestaurantListViewHolder extends RecyclerView.ViewHolder {

    private RestaurantItemBinding binding;
    private Context context;

    RestaurantListViewHolder(@NonNull RestaurantItemBinding binding, Context context) {
        super( binding.getRoot() );
        this.binding = binding;
        this.context = context;
    }

    @SuppressLint("StringFormatInvalid")
    public void updateRestaurantInfo(PlacesSearchResult placesSearchResult) {



        binding.restaurantNameTv.setText( placesSearchResult.name );
    }


}
