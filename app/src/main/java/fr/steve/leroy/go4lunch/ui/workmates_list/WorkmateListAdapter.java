package fr.steve.leroy.go4lunch.ui.workmates_list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import fr.steve.leroy.go4lunch.databinding.WorkmatesItemBinding;
import fr.steve.leroy.go4lunch.model.User;

/**
 * Created by Steve LEROY on 02/10/2021.
 */
public class WorkmateListAdapter extends RecyclerView.Adapter<WorkmateListViewHolder> {

    private List<User> workmateList;

    public WorkmateListAdapter(List<User> users) {
        this.workmateList = users;
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
        holder.updateWorkmatesInfo( workmateList.get( position ) );
    }

    @Override
    public int getItemCount() {
        return this.workmateList.size();
    }

    public void update(List<User> userList) {
        this.workmateList = userList;
        notifyDataSetChanged();
    }
}
