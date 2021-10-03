package fr.steve.leroy.go4lunch.model;

/**
 * Created by Steve LEROY on 24/09/2021.
 */
public class Restaurant {

    private String restaurantId, name, speciality, address, openingHours, websiteUrl, phoneNumber;
    private int distance;
    private double latitude, longitude;
    private boolean liked;

    public Restaurant(String restaurantId, String name, String speciality, String address, String openingHour, String openingHours, String websiteUrl, String phoneNumber, int distance, double latitude, double longitude, boolean liked) {
        this.restaurantId = restaurantId;
        this.name = name;
        this.speciality = speciality;
        this.address = address;
        this.openingHours = openingHours;
        this.websiteUrl = websiteUrl;
        this.phoneNumber = phoneNumber;
        this.distance = distance;
        this.latitude = latitude;
        this.longitude = longitude;
        this.liked = liked;
    }


    // ------- GETTERS -------
    public String getRestaurantId() {
        return restaurantId;
    }

    public String getName() {
        return name;
    }

    public String getSpeciality() {
        return speciality;
    }

    public String getAddress() {
        return address;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getDistance() {
        return distance;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public boolean isLiked() {
        return liked;
    }


    // ------- SETTERS -------
    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }
}
