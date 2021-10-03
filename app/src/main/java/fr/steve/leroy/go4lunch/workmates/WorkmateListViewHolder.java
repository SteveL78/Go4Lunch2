package fr.steve.leroy.go4lunch.workmates;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.Objects;

import fr.steve.leroy.go4lunch.R;
import fr.steve.leroy.go4lunch.databinding.WorkmatesItemBinding;
import fr.steve.leroy.go4lunch.model.User;

/**
 * Created by Steve LEROY on 25/09/2021.
 */
public class WorkmateListViewHolder extends RecyclerView.ViewHolder {

    private WorkmatesItemBinding binding;
    private Context context;

    WorkmateListViewHolder(@NonNull WorkmatesItemBinding binding, Context context) {
        super( binding.getRoot() );
        this.binding = binding;
        this.context = context;
    }

    @SuppressLint("StringFormatInvalid")
    public void updateWorkmatesInfo(User user) {
        String textUser;
        if (user != null) {
            Glide.with( context )
                    .load( user.getProfileUrl() )
                    .override( 500, 500 )
                    .centerCrop()
                    .circleCrop()
                    .into( binding.fragmentWorkmatesUserPhoto );

            if (Objects.equals( user.getRestaurantName(), null )) {
                textUser = context.getString( R.string.no_friends );
            } else if (Objects.equals( user.getRestaurantName(), "" )) {
                String message = context.getString( R.string.workmates_not_place );
                textUser = String.format( message, user.getUserName() );
            } else {
                String message = context.getString( R.string.place_workmates_eating );
                textUser = String.format( message, user.getUserName(), user.getRestaurantName() );
            }
            binding.fragmentWorkmatesSelectedRestaurantTv.setText( textUser );
        } else {
            binding.fragmentWorkmatesUserPhoto.setImageResource( R.drawable.ic_anon_user );
            binding.fragmentWorkmatesSelectedRestaurantTv.setText( R.string.no_friends );
        }

    }
}
