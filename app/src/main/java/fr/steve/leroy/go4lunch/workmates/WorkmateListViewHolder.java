package fr.steve.leroy.go4lunch.workmates;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.Objects;

import fr.steve.leroy.go4lunch.R;
import fr.steve.leroy.go4lunch.databinding.WorkmatesItemBinding;
import fr.steve.leroy.go4lunch.model.Workmate;

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
    public void updateWorkmatesInfo(Workmate workmate) {
        String textUser;
        if (workmate != null) {
            Glide.with( context )
                    .load( workmate.getProfileUrl() )
                    .override( 500, 500 )
                    .centerCrop()
                    .circleCrop()
                    .into( binding.fragmentWorkmatesUserPhoto );

            if (Objects.equals( workmate.getRestaurantName(), null )) {
                String message = context.getString( R.string.workmates_not_place );
                textUser = String.format( workmate.getFirstName() + message );
            } else if (Objects.equals( workmate.getRestaurantName(), "" )) {
                String message = context.getString( R.string.workmates_not_place );
                textUser = String.format( workmate.getFirstName() + message );
            } else {
                String message = context.getString( R.string.place_workmates_eating );
                textUser = String.format( workmate.getFirstName() + message + workmate.getRestaurantName() );
            }
            binding.fragmentWorkmatesSelectedRestaurantTv.setText( textUser );
        } else {
            binding.fragmentWorkmatesUserPhoto.setImageResource( R.drawable.ic_anon_user );
        }

    }
}
