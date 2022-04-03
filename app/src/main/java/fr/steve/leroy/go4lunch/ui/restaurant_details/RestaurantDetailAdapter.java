package fr.steve.leroy.go4lunch.ui.restaurant_details;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fr.steve.leroy.go4lunch.databinding.WorkmatesJoiningItemBinding;
import fr.steve.leroy.go4lunch.model.User;

/**
 * Created by Steve LEROY on 04/12/2021.
 */
public class RestaurantDetailAdapter extends RecyclerView.Adapter<RestaurantDetailViewHolder> {

    private List<User> listOfWorkmatesWhoChoseThisRestaurant;

    public RestaurantDetailAdapter(List<User> listOfWorkmatesWhoChoseThisRestaurant) {
        this.listOfWorkmatesWhoChoseThisRestaurant = listOfWorkmatesWhoChoseThisRestaurant;
    }

    @NonNull
    @Override
    public RestaurantDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        WorkmatesJoiningItemBinding view = WorkmatesJoiningItemBinding.inflate( LayoutInflater.from( parent.getContext() ), parent, false );
        return new RestaurantDetailViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantDetailViewHolder holder, int position) {
        holder.updateWithData( this.listOfWorkmatesWhoChoseThisRestaurant.get( position ) );
    }

    @Override
    public int getItemCount() {
        int itemCount = 0;
        if (listOfWorkmatesWhoChoseThisRestaurant != null)
            itemCount = (listOfWorkmatesWhoChoseThisRestaurant.size());
        return itemCount;
    }

    public void update(List<User> workmates) {
        this.listOfWorkmatesWhoChoseThisRestaurant = workmates;
        notifyDataSetChanged();
    }


}