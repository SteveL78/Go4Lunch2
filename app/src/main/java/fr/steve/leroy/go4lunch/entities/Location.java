package fr.steve.leroy.go4lunch.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Steve LEROY on 10/11/2021.
 */
public class Location implements Serializable {

    @SerializedName("lat")
    private Double lat;

    @SerializedName("lng")
    private Double lng;


    // ------- GETTERS -------
    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }


    // ------- SETTERS -------
    public void setLat(Double lat) {
        this.lat = lat;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

}