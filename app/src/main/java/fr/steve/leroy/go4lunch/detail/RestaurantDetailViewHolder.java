package fr.steve.leroy.go4lunch.detail;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import fr.steve.leroy.go4lunch.R;
import fr.steve.leroy.go4lunch.databinding.WorkmatesJoiningItemBinding;
import fr.steve.leroy.go4lunch.model.Workmate;

/**
 * Created by Steve LEROY on 04/12/2021.
 */
public class RestaurantDetailViewHolder extends RecyclerView.ViewHolder {


    private WorkmatesJoiningItemBinding binding;

    public RestaurantDetailViewHolder(WorkmatesJoiningItemBinding itemView) {
        super( itemView.getRoot() );
        this.binding = itemView;
    }

    public void updateWithData(Workmate result) {
        RequestManager glide = Glide.with( itemView );

        if (!(result.getProfileUrl() == null)) {
            glide.load( result.getProfileUrl() )
                    .apply( RequestOptions.circleCropTransform() )
                    .into( binding.workmatesJoiningImg );
        } else {
            glide.load( R.drawable.ic_anon_user )
                    .apply( RequestOptions.circleCropTransform() )
                    .into( binding.workmatesJoiningImg );
        }
        binding.workmatesJoiningNameTv.setText( itemView.getResources().getString( R.string.text_workmate_joining, result.getFirstName() ) );
    }













    /*
    public RestaurantDetailViewHolder(@NonNull View itemView) {
        super( itemView );
       // mImageView = workmatesBinding.workmatesJoiningImg;
        mImageView = itemView.findViewById( R.id.workmates_joining_img );
        // mTextView = workmatesBinding.workmatesJoiningImg;
        mTextView = itemView.findViewById( R.id.workmates_joining_name_tv );
    }
*/
/*
    public void updateWithData(Workmate workmate) {
        RequestManager glide = Glide.with( itemView );

        //Photo
        glide.load( workmate.getProfileUrl() )
                .apply( RequestOptions.circleCropTransform() )
                .into( binding.workmatesJoiningImg );

        //Name
        binding.workmatesJoiningNameTv.setText( workmate.getFirstName() );

    }


 */




    /*
        public void updateWithData(PlacesSearchResult placesSearchResult, List<Workmate> workmateList) {

            displayRestaurantPhoto( placesSearchResult );


        }

        private void displayRestaurantPhoto(PlacesSearchResult placesSearchResult) {

            if (placesSearchResult.photos != null && placesSearchResult.photos.length > 0) {

                Log.d( "photos", placesSearchResult.photos[0].toString() );

                String imageurl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + placesSearchResult.photos[0].photoReference + "&key=" + context.getString( R.string.google_maps_API_key );

                Glide.with( context )
                        .load( imageurl )
                        .centerCrop()
                        .into( activityBinding.activityDetailRestaurantImg );
            } else {
                activityBinding.activityDetailRestaurantImg.setImageResource( R.drawable.image_not_avalaible );
            }
        }
    */


}