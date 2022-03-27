package fr.steve.leroy.go4lunch.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

/**
 * Created by Steve LEROY on 12/01/2022.
 */
public class Booking {

    private String uid;
    private String placeId;
    private String restaurantName;
    private Date dateCreated;


    public Booking(String uid, String placeId, String restaurantName) {
        this.uid = uid;
        this.placeId = placeId;
        this.restaurantName = restaurantName;
    }


    // ------- GETTERS -------

    public String getUid() {
        return uid;
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    @ServerTimestamp
    public Date getDateCreated() {
        return dateCreated;
    }


    // ------- SETTERS -------

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}
