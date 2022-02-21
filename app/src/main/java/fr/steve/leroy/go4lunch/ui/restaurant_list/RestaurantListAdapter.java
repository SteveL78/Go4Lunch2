package fr.steve.leroy.go4lunch.ui.restaurant_list;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.maps.model.PlacesSearchResult;

import java.util.List;

import fr.steve.leroy.go4lunch.databinding.RestaurantItemBinding;
import fr.steve.leroy.go4lunch.model.Workmate;

/**
 * Created by Steve LEROY on 26/09/2021.
 */
public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListViewHolder> {


    private List<PlacesSearchResult> placesSearchResults;
    private List<Workmate> workmateList;
    private Location currentLocation = null;
    private OnRestaurantClickListener listener;

    public interface OnRestaurantClickListener {
        void onRestaurantClick(PlacesSearchResult result);
    }

    public RestaurantListAdapter(List<PlacesSearchResult> placesSearchResults, OnRestaurantClickListener listener) {
        this.placesSearchResults = placesSearchResults;
        this.listener = listener;
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

        holder.itemView.setOnClickListener( v -> {
            PlacesSearchResult result = placesSearchResults.get( position );
            listener.onRestaurantClick( result );
        } );
    }

    @Override
    public int getItemCount() {
        return this.placesSearchResults.size();
    }

    public void updateWithData(List<Workmate> workmateList, List<PlacesSearchResult> restaurantList, Location currentLocation) {
        this.workmateList = workmateList;
        this.placesSearchResults = restaurantList;
        this.currentLocation = currentLocation;
        notifyDataSetChanged();
    }
}