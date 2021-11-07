package fr.steve.leroy.go4lunch.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.maps.model.PlacesSearchResult;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import fr.steve.leroy.go4lunch.databinding.RestaurantItemBinding;
import fr.steve.leroy.go4lunch.databinding.WorkmatesItemBinding;
import fr.steve.leroy.go4lunch.model.Restaurant;
import fr.steve.leroy.go4lunch.model.Workmate;
import fr.steve.leroy.go4lunch.workmates.WorkmateListViewHolder;

/**
 * Created by Steve LEROY on 26/09/2021.
 */
public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListViewHolder>{


    private List<PlacesSearchResult> mPlacesSearchResults;

    public RestaurantListAdapter(List<PlacesSearchResult> placesSearchResults) {
        this.mPlacesSearchResults = placesSearchResults;
    }


    @NotNull
    @Override
    public RestaurantListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from( parent.getContext() );
        Context context = parent.getContext();
        return new RestaurantListViewHolder( RestaurantItemBinding.inflate( layoutInflater ), context );
    }


    @Override
    public void onBindViewHolder(RestaurantListViewHolder holder, int position) {
        holder.updateRestaurantInfo( mPlacesSearchResults.get( position ) );
    }

    @Override
    public int getItemCount() {
        return this.mPlacesSearchResults.size();
    }

    public void update(List<PlacesSearchResult> placesSearchResults) {
        this.mPlacesSearchResults = placesSearchResults;
        notifyDataSetChanged();
    }
}