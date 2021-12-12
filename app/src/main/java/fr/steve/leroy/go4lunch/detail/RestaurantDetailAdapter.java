package fr.steve.leroy.go4lunch.detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fr.steve.leroy.go4lunch.R;
import fr.steve.leroy.go4lunch.model.Workmate;

/**
 * Created by Steve LEROY on 04/12/2021.
 */
public class RestaurantDetailAdapter extends RecyclerView.Adapter<RestaurantDetailViewHolder> {

    private List<Workmate> workmateList;

    public RestaurantDetailAdapter(List<Workmate> result) {
        this.workmateList = result;
    }

    @NonNull
    @Override
    public RestaurantDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from( context );
        View view = inflater.inflate( R.layout.workmates_joining_item, parent, false );
        return new RestaurantDetailViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantDetailViewHolder holder, int position) {
        holder.updateWithData( this.workmateList.get( position ) );

    }

    @Override
    public int getItemCount() {
        return workmateList.size();
    }

}