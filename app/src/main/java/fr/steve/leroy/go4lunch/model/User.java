package fr.steve.leroy.go4lunch.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Steve LEROY on 24/09/2021.
 */
public class User implements Parcelable {

    private String userId, userName, placeId, profileUrl, restaurantId, restaurantName;

    public User() {
        // Empty constructor
    }

    public User(String userId, String userName, String placeId, String profileUrl, String restaurantId, String restaurantName) {
        this.userId = userId;
        this.userName = userName;
        this.placeId = placeId;
        this.profileUrl = profileUrl;
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
    }


    // ------- GETTERS -------
    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }


    // ------- SETTERS -------
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    // PARCELABLE
    protected User(Parcel in) {
        userId = in.readString();
        userName = in.readString();
        placeId = in.readString();
        profileUrl = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User( in );
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString( userId );
        dest.writeString( userName );
        dest.writeString( placeId );
        dest.writeString( profileUrl );
    }
}
