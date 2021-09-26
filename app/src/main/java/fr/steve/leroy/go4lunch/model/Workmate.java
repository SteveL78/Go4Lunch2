package fr.steve.leroy.go4lunch.model;

import java.io.Serializable;

/**
 * Created by Steve LEROY on 24/09/2021.
 */
public class Workmate implements Serializable {

    private String userId, userName, placeId, profileUrl;

    public Workmate(String userId, String userName, String placeId, String profileUrl) {
        this.userId = userId;
        this.userName = userName;
        this.placeId = placeId;
        this.profileUrl = profileUrl;
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

}
