package fr.steve.leroy.go4lunch.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

/**
 * Created by Steve LEROY on 12/01/2022.
 */
public class Booking {

    private String uid;
    private String username;
    private String placeId;
    private String restaurantName;


    public Booking(String uid, String username, String placeId, String restaurantName) {
        this.uid = uid;
        this.username = username;
        this.placeId = placeId;
        this.restaurantName = restaurantName;
    }
}