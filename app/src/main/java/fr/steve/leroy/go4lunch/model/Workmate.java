package fr.steve.leroy.go4lunch.model;

import com.google.firebase.firestore.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Steve LEROY on 24/09/2021.
 */
@IgnoreExtraProperties
public class Workmate implements Serializable {

    private String uid, firstName, profileUrl, placeId, restaurantName;

    // Empty constructor needed for the recyclerView
    public Workmate() {
    }

    public Workmate(String uid, String firstName, String profileUrl) {
        this.uid = uid;
        this.firstName = firstName;
        this.profileUrl = profileUrl;
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


    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }


}