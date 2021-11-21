package fr.steve.leroy.go4lunch.list;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.maps.model.PlacesSearchResult;

import java.util.List;

import fr.steve.leroy.go4lunch.databinding.RestaurantItemBinding;

/**
 * Created by Steve LEROY on 26/09/2021.
 */
public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListViewHolder> {


    private List<PlacesSearchResult> placesSearchResults;
    private Location currentLocation = null;

    public RestaurantListAdapter(List<PlacesSearchResult> placesSearchResults, Context context) {
        this.placesSearchResults = placesSearchResults;
    }

    @NonNull
    @Override
    public RestaurantListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from( parent.getContext() );
        Context context = parent.getContext();
        return new RestaurantListViewHolder( RestaurantItemBinding.inflate( layoutInflater ), context );
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantListViewHolder holder, int position) {

        holder.updateRestaurantInfo( placesSearchResults.get( position ), currentLocation );
    }

    @Override
    public int getItemCount() {
        return this.placesSearchResults.size();
    }

    public void update(List<PlacesSearchResult> restaurantList, Location currentLocation) {
        this.placesSearchResults = restaurantList;
        this.currentLocation = currentLocation;
        notifyDataSetChanged();
    }
}