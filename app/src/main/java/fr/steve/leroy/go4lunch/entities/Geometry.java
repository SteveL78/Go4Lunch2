package fr.steve.leroy.go4lunch.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Steve LEROY on 10/11/2021.
 */
public class Geometry implements Serializable {

    @SerializedName("location")
    private Location location;

    // ------- GETTER -------
    public Location getLocation() {
        return location;
    }


    // ------- SETTER -------
    public void setLocation(Location location) {
        this.location = location;
    }

}
