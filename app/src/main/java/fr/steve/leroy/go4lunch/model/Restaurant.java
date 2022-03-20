package fr.steve.leroy.go4lunch.model;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steve LEROY on 20/03/2022.
 */
public class Restaurant {

    private String placeId;
    private String name;
    private Double latitude;
    private Double longitude;
    private String address;
    private boolean openNow;
    private int distance;
    private String urlPicture;
    private float rating;
    private String phoneNumber;
    private String webSite;
    private List<User> workmatesEatingHere;

    public Restaurant(){ }

    public Restaurant(String placeId, String name, Double latitude, Double longitude, @Nullable String address,
                      boolean openNow, int distance, @Nullable String urlPicture, float rating, String phoneNumber, String webSite) {
        this.placeId = placeId;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.openNow = openNow;
        this.distance = distance;
        this.urlPicture = urlPicture;
        this.rating = rating;
        this.phoneNumber = phoneNumber;
        this.webSite = webSite;
        this.workmatesEatingHere = new ArrayList<>();
    }


    // ------- GETTERS -------
    public String getPlaceId() {
        return placeId;
    }

    public String getName() {
        return name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return address;
    }

    public boolean isOpenNow() {
        return openNow;
    }

    public int getDistance() {
        return distance;
    }

    public String getUrlPicture() {
        return urlPicture;
    }

    public float getRating() {
        return rating;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getWebSite() {
        return webSite;
    }

    public List<User> getWorkmatesEatingHere() {
        return workmatesEatingHere;
    }


    // ------- SETTERS -------
    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setOpenNow(boolean openNow) {
        this.openNow = openNow;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setUrlPicture(String urlPicture) {
        this.urlPicture = urlPicture;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public void setWorkmatesEatingHere(List<User> workmatesEatingHere) {
        this.workmatesEatingHere = workmatesEatingHere;
    }
}
