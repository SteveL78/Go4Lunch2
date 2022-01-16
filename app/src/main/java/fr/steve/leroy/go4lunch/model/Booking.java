package fr.steve.leroy.go4lunch.model;

/**
 * Created by Steve LEROY on 12/01/2022.
 */
public class Booking {

    private String bookingData;
    private String workmateId;
    private String restaurantId;
    private String restaurantName;

    public Booking(String bookingData, String workmateId, String restaurantId, String restaurantName) {
        this.bookingData = bookingData;
        this.workmateId = workmateId;
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
    }


    // ------- GETTERS -------

    public String getBookingData() {
        return bookingData;
    }

    public String getWorkmateId() {
        return workmateId;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }


    // ------- SETTERS -------

    public void setBookingData(String bookingData) {
        this.bookingData = bookingData;
    }

    public void setWorkmateId(String workmateId) {
        this.workmateId = workmateId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }
}
