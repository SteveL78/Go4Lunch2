package fr.steve.leroy.go4lunch.model;

import androidx.annotation.Nullable;

import java.util.List;

/**
 * Created by Steve LEROY on 19/03/2022.
 */
public class User {

    private String uid;
    private String username;
    @Nullable
    private String urlPicture;
    private String placeId;
    private String restaurantName;

    public User() { /* Required empty public constructor for the recyclerView */ }

    public User(String uid, String username, @Nullable String urlPicture, String placeId, String restaurantName) {
        this.uid = uid;
        this.username = username;
        this.urlPicture = urlPicture;
        this.placeId = placeId;
        this.restaurantName = restaurantName;
    }

    public User(String uid, String username, @Nullable String urlPicture) {
        this.uid = uid;
        this.username = username;
        this.urlPicture = urlPicture;
    }


    // --- GETTERS ---

    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    @Nullable
    public String getUrlPicture() {
        return urlPicture;
    }


    // --- SETTERS ---

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public void setUrlPicture(@Nullable String urlPicture) {
        this.urlPicture = urlPicture;
    }

}