package fr.steve.leroy.go4lunch.detail;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fr.steve.leroy.go4lunch.databinding.WorkmatesJoiningItemBinding;
import fr.steve.leroy.go4lunch.model.Workmate;

/**
 * Created by Steve LEROY on 04/12/2021.
 */
public class RestaurantDetailAdapter extends RecyclerView.Adapter<RestaurantDetailViewHolder> {

    private List<Workmate> mWorkmateList;

    public RestaurantDetailAdapter(List<Workmate> mWorkmateList) {
        this.mWorkmateList = mWorkmateList;
    }

    @NonNull
    @Override
    public RestaurantDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        WorkmatesJoiningItemBinding view = WorkmatesJoiningItemBinding.inflate( LayoutInflater.from( parent.getContext() ), parent, false );
        return new RestaurantDetailViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantDetailViewHolder holder, int position) {
        holder.updateWithData( this.mWorkmateList.get( position ) );

    }

    @Override
    public int getItemCount() {
        int itemCount = 0;
        if (mWorkmateList != null) itemCount = (mWorkmateList.size());
        return itemCount;
        //return mWorkmateList.size();
    }

    public void update(List<Workmate> workmates) {
        this.mWorkmateList = workmates;
        notifyDataSetChanged();
    }


}