package fr.steve.leroy.go4lunch.detail;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import fr.steve.leroy.go4lunch.R;
import fr.steve.leroy.go4lunch.model.Workmate;

/**
 * Created by Steve LEROY on 04/12/2021.
 */
public class RestaurantDetailViewHolder extends RecyclerView.ViewHolder {

    private ImageView mImageView;
    TextView mTextView;

    public RestaurantDetailViewHolder(@NonNull View itemView) {
        super( itemView );
        mImageView = itemView.findViewById( R.id.workmates_joining_img );
        mTextView = itemView.findViewById( R.id.workmates_joining_name_tv );
    }

    public void updateWithData(Workmate result) {
        RequestManager glide = Glide.with( itemView );

        if (!(result.getProfileUrl() == null)) {
            glide.load( result.getProfileUrl() )
                    .apply( RequestOptions.circleCropTransform() )
                    .into( mImageView );

        } else {
            glide.load( R.drawable.ic_anon_user )
                    .apply( RequestOptions.circleCropTransform() )
                    .into( mImageView );
        }

        this.mTextView.setText( itemView.getResources().getString( R.string.text_workmate_joining, result.getFirstName() ) );
    }

}