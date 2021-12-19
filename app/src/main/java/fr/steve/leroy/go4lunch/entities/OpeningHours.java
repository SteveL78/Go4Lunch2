package fr.steve.leroy.go4lunch.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steve LEROY on 10/11/2021.
 */
public class OpeningHours implements Serializable {

    @SerializedName("open_now")
    @Expose
    private Boolean openNow;

    @SerializedName("weekday_text")
    @Expose
    private List<Object> weekdayText = new ArrayList<Object>();


    // ------- GETTERS -------

    /**
     * @return The openNow
     */
    public Boolean getOpenNow() {
        return openNow;
    }

    /**
     * @return The weekdayText
     */
    public List<Object> getWeekdayText() {
        return weekdayText;
    }


    // ------- SETTERS -------

    /**
     * @param openNow The open_now
     */
    public void setOpenNow(Boolean openNow) {
        this.openNow = openNow;
    }

    /**
     * @param weekdayText The weekday_text
     */
    public void setWeekdayText(List<Object> weekdayText) {
        this.weekdayText = weekdayText;
    }

}