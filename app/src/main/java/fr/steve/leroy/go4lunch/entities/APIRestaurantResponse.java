package fr.steve.leroy.go4lunch.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Steve LEROY on 13/11/2021.
 */
public class APIRestaurantResponse {

    @SerializedName("results")
    private List<RestaurantAPI> results;

    @SerializedName("status")
    private String status;


    // ------- GETTER -------
    public List<RestaurantAPI> getResults() {
        return results;
    }

    public String getStatus() {
        return status;
    }


    // ------- SETTER -------
    public void setResults(List<RestaurantAPI> results) {
        this.results = results;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}