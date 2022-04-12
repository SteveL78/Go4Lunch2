package fr.steve.leroy.go4lunch.model;

/**
 * Created by Steve LEROY on 20/03/2022.
 */
public class Like {

    private String uid;
    private String username;
    private String placeId;
    private String restaurantName;
    private boolean isLiked;


    public Like() { /* Required empty public constructor for the recyclerView */ }

    public Like(String uid, String username, String placeId, String restaurantName) {
        this.uid = uid;
        this.username = username;
        this.placeId = placeId;
        this.restaurantName = restaurantName;
        this.isLiked = false;
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

    public boolean isLiked() { return isLiked; }


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

    public void setLiked(boolean liked) { isLiked = liked; }
}
