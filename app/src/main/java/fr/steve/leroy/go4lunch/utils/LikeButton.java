package fr.steve.leroy.go4lunch.utils;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.maps.model.PlaceDetails;

import fr.steve.leroy.go4lunch.R;
import fr.steve.leroy.go4lunch.firebase.RestaurantHelper;

/**
 * Created by Steve LEROY on 21/01/2022.
 */
public class LikeButton {

    public static void likeRestaurant(PlaceDetails placeDetails, Context context, TextView textView, String unlike, String liked) {
        if (placeDetails != null && FirebaseAuth.getInstance().getCurrentUser() != null) {
            RestaurantHelper.createLike( placeDetails.placeId, placeDetails.name, FirebaseAuth.getInstance().getCurrentUser().getUid(), FirebaseAuth.getInstance().getCurrentUser().getDisplayName()).addOnCompleteListener( (Task<Void> likeTask) -> {
                if (likeTask.isSuccessful()) {
                    textView.setText(unlike);
                    Toast.makeText(context, liked, Toast.LENGTH_SHORT).show();
                    // TODO : ajouter ce restaurant à la liste des restaurants likés du User
                }
            });
        } else {
            Toast.makeText(context, liked, Toast.LENGTH_SHORT).show();
        }
    }

    public static void dislikeRestaurant(PlaceDetails placeDetails, Context context, TextView textView, String like, String disliked, String liked) {
        if (placeDetails != null) {
            RestaurantHelper.deleteLike(placeDetails.placeId, FirebaseAuth.getInstance().getUid());
            textView.setText(like);
            Toast.makeText(context, disliked, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, liked, Toast.LENGTH_SHORT).show();
        }
    }
}