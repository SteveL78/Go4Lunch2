package fr.steve.leroy.go4lunch.utils;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.maps.model.PlaceDetails;

import fr.steve.leroy.go4lunch.R;
import fr.steve.leroy.go4lunch.firebase.LikeHelper;

/**
 * Created by Steve LEROY on 21/01/2022.
 */
public class LikeButton extends AppCompatActivity {

    public static void likeRestaurant(PlaceDetails placeDetails, Context context, TextView textView, String unlike, String liked) {
        if (placeDetails != null && FirebaseAuth.getInstance().getCurrentUser() != null) {
            LikeHelper.createLike( placeDetails.placeId, placeDetails.name, FirebaseAuth.getInstance().getCurrentUser().getUid(), FirebaseAuth.getInstance().getCurrentUser().getDisplayName() ).addOnCompleteListener( (Task<Void> likeTask) -> {
                if (likeTask.isSuccessful()) {
                    textView.setText( unlike );
                    Toast.makeText( context, liked, Toast.LENGTH_SHORT ).show();
                }
            } );
        } else {
            Toast.makeText( context, liked, Toast.LENGTH_SHORT ).show();
        }
    }

    public static void dislikeRestaurant(PlaceDetails placeDetails, Context context, TextView textView, String like, String disliked, String liked) {
        if (placeDetails != null) {
            LikeHelper.deleteLike( placeDetails.placeId, FirebaseAuth.getInstance().getUid() );
            textView.setText( like );
            Toast.makeText( context, disliked, Toast.LENGTH_SHORT ).show();
        } else {
            Toast.makeText( context, liked, Toast.LENGTH_SHORT ).show();
        }
    }

    // Todo : call this onCreate
    public static void checkLikeRestaurant(String uid, String placeId, View view) {
        LikeHelper.getAllLikeByUserId( uid, placeId )
                .addOnCompleteListener( checkTask -> {
                    if (checkTask.isSuccessful() && checkTask.getResult().size() != 0) {
                        // TODO : Display like
                        Button likeBtn = view.findViewById( R.id.like_btn );
                        likeBtn.setText( R.string.detail_restaurant_unlike );
                        //likeBtn.setCompoundDrawablesWithIntrinsicBounds( null, getResources().getDrawable( ic_baseline_star_orange_24 ), null, null );

                    }
                } );
    }
}