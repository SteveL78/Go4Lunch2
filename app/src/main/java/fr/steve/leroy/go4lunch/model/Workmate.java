package fr.steve.leroy.go4lunch.model;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.IgnoreExtraProperties;

import java.io.Serializable;

/**
 * Created by Steve LEROY on 24/09/2021.
 */
@IgnoreExtraProperties
public class Workmate implements Serializable {

    private String workmateId, firstName, placeId, restaurantName;
    @Nullable
    private String profileUrl = "";

    public Workmate() { /* Required empty public constructor for the recyclerView */ }

    public Workmate(String workmateId, String firstName, String profileUrl) {
        this.workmateId = workmateId;
        this.firstName = firstName;
        this.profileUrl = profileUrl;
    }


    // ------- GETTERS -------
    public String getWorkmateId() {
        return workmateId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getPlaceId() {
        return placeId;
    }

    @Nullable
    public String getProfileUrl() {
        return profileUrl;
    }

    public String getRestaurantName() {
        return restaurantName;
    }


    // ------- SETTERS -------
    public void setWorkmateId(String workmateId) {
        this.workmateId = workmateId;
    }

    public void setFirstName(String workmateName) {
        this.firstName = workmateName;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public void setProfileUrl(@Nullable String profileUrl) {
        this.profileUrl = profileUrl;
    }


    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }


}