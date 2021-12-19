package fr.steve.leroy.go4lunch.detail;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import fr.steve.leroy.go4lunch.R;
import fr.steve.leroy.go4lunch.databinding.ActivityRestaurantDetailBinding;
import fr.steve.leroy.go4lunch.databinding.WorkmatesJoiningItemBinding;
import fr.steve.leroy.go4lunch.model.Workmate;

/**
 * Created by Steve LEROY on 04/12/2021.
 */
public class RestaurantDetailViewHolder extends RecyclerView.ViewHolder {


    private ActivityRestaurantDetailBinding activityBinding;
    private WorkmatesJoiningItemBinding workmatesBinding;

    private ImageView mImageView;
    private TextView mTextView;

    public RestaurantDetailViewHolder(@NonNull View itemView) {
        super( itemView );
        mImageView = itemView.findViewById( R.id.workmates_joining_img );
        mTextView = itemView.findViewById( R.id.workmates_joining_name_tv );
    }


    public void updateWithData(Workmate workmate) {
        RequestManager glide = Glide.with( itemView );

        glide.load( workmate.getProfileUrl() )
                .apply( RequestOptions.circleCropTransform() )
                .into( workmatesBinding.workmatesJoiningImg );

        workmatesBinding.workmatesJoiningNameTv.setText( workmate.getFirstName() );
    }





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