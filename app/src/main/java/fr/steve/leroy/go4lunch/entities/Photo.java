package fr.steve.leroy.go4lunch.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steve LEROY on 10/11/2021.
 */
public class Photo implements Serializable {

    @SerializedName("height")
    private Integer height;

    @SerializedName("width")
    private Integer width;

    @SerializedName("html_attributions")
    private List<String> htmlAttributions = new ArrayList<String>();

    @SerializedName("photo_reference")
    private String photoReference;


    // ------- GETTERS -------

    /**
     * @return The height
     */
    public Integer getHeight() {
        return height;
    }

    /**
     * @return The htmlAttributions
     */
    public List<String> getHtmlAttributions() {
        return htmlAttributions;
    }

    /**
     * @return The photoReference
     */
    public String getPhotoReference() {
        return photoReference;
    }

    /**
     * @return The width
     */
    public Integer getWidth() {
        return width;
    }


    // ------- SETTERS -------

    /**
     * @param height The height
     */
    public void setHeight(Integer height) {
        this.height = height;
    }

    /**
     * @param htmlAttributions The html_attributions
     */
    public void setHtmlAttributions(List<String> htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }

    /**
     * @param photoReference The photo_reference
     */
    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }

    /**
     * @param width The width
     */
    public void setWidth(Integer width) {
        this.width = width;
    }

}