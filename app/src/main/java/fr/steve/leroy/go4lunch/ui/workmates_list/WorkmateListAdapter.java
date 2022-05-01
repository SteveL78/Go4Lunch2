package fr.steve.leroy.go4lunch.ui.workmates_list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import fr.steve.leroy.go4lunch.databinding.WorkmatesItemBinding;
import fr.steve.leroy.go4lunch.model.User;

/**
 * Created by Steve LEROY on 02/10/2021.
 */
public class WorkmateListAdapter extends RecyclerView.Adapter<WorkmateListViewHolder> implements Filterable {

    private List<User> workmatesEatingHere;
    private List<User> workmatesEatingHereFull;

    public WorkmateListAdapter(List<User> workmatesEatingHere) {
        this.workmatesEatingHere = workmatesEatingHere;
        this.workmatesEatingHereFull = new ArrayList<>( workmatesEatingHere );
    }

    @NotNull
    @Override
    public WorkmateListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from( parent.getContext() );
        Context context = parent.getContext();
        return new WorkmateListViewHolder( WorkmatesItemBinding.inflate( layoutInflater ), context );
    }


    @Override
    public void onBindViewHolder(WorkmateListViewHolder holder, int position) {
        holder.updateWorkmatesInfo( workmatesEatingHere.get( position ) );
    }

    @Override
    public int getItemCount() {
        return this.workmatesEatingHere.size();
    }

    public void update(List<User> userList) {
        this.workmatesEatingHere = userList;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        // Run on background thread
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<User> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0 ) {
                filteredList.addAll(workmatesEatingHereFull );
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (User item : workmatesEatingHereFull){
                    if (item.getUsername().toLowerCase().contains( filterPattern )) {
                        filteredList.add( item );
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        // runs in ui thread
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            workmatesEatingHere.clear();
            workmatesEatingHere.addAll( (List) results.values );
            notifyDataSetChanged();
        }
    };
}
