package fr.steve.leroy.go4lunch.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

/**
 * Created by Steve LEROY on 24/09/2021.
 */
@IgnoreExtraProperties
public class Workmate implements Parcelable {

    private String workmateId,firstName, placeId, profileUrl, restaurantId, restaurantName;
    private @ServerTimestamp Date timestamp;

    public Workmate(String workmateId, String firstName, String placeId, String profileUrl, String restaurantId, String restaurantName, Date timestamp) {
        this.workmateId = workmateId;
        this.firstName = firstName;
        this.placeId = placeId;
        this.profileUrl = profileUrl;
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.timestamp = timestamp;
    }

    public Workmate(){
        //empty constructor needed
    }

    protected Workmate(Parcel in) {
        workmateId = in.readString();
        firstName = in.readString();
        placeId = in.readString();
        profileUrl = in.readString();
        restaurantId = in.readString();
        restaurantName = in.readString();
    }

    public static final Creator<Workmate> CREATOR = new Creator<Workmate>() {
        @Override
        public Workmate createFromParcel(Parcel in) {
            return new Workmate(in);
        }

        @Override
        public Workmate[] newArray(int size) {
            return new Workmate[size];
        }
    };


    // ------- GETTERS -------
    public String getWorkmateId() {
        return workmateId;
    }

    public String getFirstName() {
        return firstName;
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

    public Date getTimestamp() {
        return timestamp;
    }

    // ------- SETTERS -------
    public void setWorkmateId(String workmateId) {
        this.workmateId = workmateId;
    }

    public void setFirstName(String workmateName) {
        this.firstName = workmateName;
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

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(workmateId);
        parcel.writeString(firstName);
        parcel.writeString(placeId);
        parcel.writeString(profileUrl);
        parcel.writeString(restaurantId);
        parcel.writeString(restaurantName);
    }
}
