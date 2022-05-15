package fr.steve.leroy.go4lunch.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.content.res.AppCompatResources;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.maps.model.PlaceDetails;

import java.util.Objects;

import fr.steve.leroy.go4lunch.R;
import fr.steve.leroy.go4lunch.firebase.LikeHelper;
import fr.steve.leroy.go4lunch.manager.UserManager;

/**
 * Created by Steve LEROY on 21/01/2022.
 */
public class LikeButton {

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

}