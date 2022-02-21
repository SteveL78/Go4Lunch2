package fr.steve.leroy.go4lunch.model;

import com.google.firebase.firestore.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Steve LEROY on 24/09/2021.
 */
@IgnoreExtraProperties
public class Workmate implements Serializable {

    private String uid, firstName, placeId, profileUrl, restaurantId, restaurantName;
    private boolean favorite;


    public Workmate(String uid, String firstName, String placeId, String profileUrl, String restaurantId, String restaurantName) {
        this.uid = uid;
        this.firstName = firstName;
        this.placeId = placeId;
        this.profileUrl = profileUrl;
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;

    }

    //empty constructor needed for the recyclerView
    public Workmate() {
    }

    public boolean isFavorite() {
        return favorite;
    }


    // ------- GETTERS -------
    public String getWorkmateId() {
        return uid;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }


    // ------- SETTERS -------
    public void setWorkmateId(String workmateId) {
        this.uid = workmateId;
    }

    public void setFirstName(String workmateName) {
        this.firstName = workmateName;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

}