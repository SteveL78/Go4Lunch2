package fr.steve.leroy.go4lunch.ui.restaurant_details;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import fr.steve.leroy.go4lunch.R;
import fr.steve.leroy.go4lunch.databinding.WorkmatesJoiningItemBinding;
import fr.steve.leroy.go4lunch.model.User;

/**
 * Created by Steve LEROY on 04/12/2021.
 */
public class RestaurantDetailViewHolder extends RecyclerView.ViewHolder {

    private WorkmatesJoiningItemBinding binding;

    public RestaurantDetailViewHolder(WorkmatesJoiningItemBinding itemView) {
        super( itemView.getRoot() );
        this.binding = itemView;
    }

    public void updateWithData(User result) {
        RequestManager glide = Glide.with( itemView );

        if (!(result.getUrlPicture() == null)) {
            glide.load( result.getUrlPicture() )
                    .apply( RequestOptions.circleCropTransform() )
                    .into( binding.workmatesJoiningImg );
        } else {
            glide.load( R.drawable.ic_anon_user )
                    .apply( RequestOptions.circleCropTransform() )
                    .into( binding.workmatesJoiningImg );
        }
        binding.workmatesJoiningNameTv.setText( itemView.getResources().getString( R.string.text_workmate_joining, result.getUsername() ) );
    }

}