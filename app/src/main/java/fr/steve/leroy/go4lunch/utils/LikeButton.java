package fr.steve.leroy.go4lunch.utils;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.maps.model.PlacesSearchResult;

/**
 * Created by Steve LEROY on 19/02/2022.
 */
public class LikeButton {
/*
    public static void likeRestaurant(PlacesSearchResult result, Context context, TextView textView, String unlike, String liked) {
        if (result != null && FirebaseAuth.getInstance().getCurrentUser() != null) {
            RestaurantsHelper.createLike(result.placeId(), FirebaseAuth.getInstance()
                    .getCurrentUser()
                    .getUid())
                    .addOnCompleteListener(likeTask -> {
                if (likeTask.isSuccessful()) {
                    Toast.makeText(context, liked, Toast.LENGTH_SHORT).show();
                    textView.setText(unlike);
                }
            });
        } else {
            Toast.makeText(context, liked, Toast.LENGTH_SHORT).show();
        }
    }

    public static void dislikeRestaurant(PlacesSearchResult result, Context context, TextView textView, String like, String disliked, String liked) {
        if (result != null) {
            RestaurantsHelper.deleteLike(result.placeId(), FirebaseAuth.getInstance().getUid());
            textView.setText(like);
            Toast.makeText(context, disliked, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, liked, Toast.LENGTH_SHORT).show();
        }
    }

 */
}
