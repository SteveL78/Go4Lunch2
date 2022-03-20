package fr.steve.leroy.go4lunch.model;

/**
 * Created by Steve LEROY on 20/03/2022.
 */
public class FavoriteRestaurant {

    private String uid;
    private String username;
    private String placeId;
    private String restaurantName;

    public FavoriteRestaurant() { /* Required empty public constructor for the recyclerView */ }

    public FavoriteRestaurant(String uid, String username, String placeId, String restaurantName) {
        this.uid = uid;
        this.username = username;
        this.placeId = placeId;
        this.restaurantName = restaurantName;
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

}
