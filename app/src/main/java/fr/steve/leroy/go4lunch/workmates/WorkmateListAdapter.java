package fr.steve.leroy.go4lunch.workmates;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import fr.steve.leroy.go4lunch.databinding.WorkmatesItemBinding;
import fr.steve.leroy.go4lunch.model.Workmate;

/**
 * Created by Steve LEROY on 02/10/2021.
 */
public class WorkmateListAdapter extends RecyclerView.Adapter<WorkmateListViewHolder> {

    private List<Workmate> userList;

    public WorkmateListAdapter(List<Workmate> users) {
        this.userList = users;
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
        holder.updateWorkmatesInfo( userList.get( position ) );
    }

    @Override
    public int getItemCount() {
        return this.userList.size();
    }

    public void update(List<Workmate> users) {
        this.userList = users;
        notifyDataSetChanged();
    }
}
