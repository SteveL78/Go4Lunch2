package fr.steve.leroy.go4lunch.model;

import java.util.List;

/**
 * Created by Steve LEROY on 20/03/2022.
 */
public class FavoriteRestaurant {

    private String uid;
    private String username;
    private String placeId;
    private String restaurantName;
    private List<String> likedRestaurantList;

    public FavoriteRestaurant() { /* Required empty public constructor for the recyclerView */ }

    public FavoriteRestaurant(String uid, String username, String placeId, String restaurantName, List<String> likedRestaurantList) {
        this.uid = uid;
        this.username = username;
        this.placeId = placeId;
        this.restaurantName = restaurantName;
        this.likedRestaurantList = likedRestaurantList;
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

    public List<String> getLikedRestaurantList() {
        return likedRestaurantList;
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

    public void setLikedRestaurantList(List<String> likedRestaurantList) {
        this.likedRestaurantList = likedRestaurantList;
    }
}
