package fr.steve.leroy.go4lunch.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Steve LEROY on 13/11/2021.
 */
public class APIDetailsRestaurantResponse {
    @SerializedName("result")
    private RestaurantAPI result;
    @SerializedName("status")
    private String status;

    public RestaurantAPI getResult() { return result; }

    public void setResult(RestaurantAPI result) { this.result = result; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }
}
