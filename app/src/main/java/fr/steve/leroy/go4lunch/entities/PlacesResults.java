package fr.steve.leroy.go4lunch.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steve LEROY on 10/11/2021.
 */
public class PlacesResults implements Serializable {

    @SerializedName("html_attributions")
    private List<Object> htmlAttributions = new ArrayList<Object>();

    @SerializedName("next_page_token")
    private String nextPageToken;

    @SerializedName("results")
    private List<Result> results = new ArrayList<Result>();

    @SerializedName("status")
    private String status;


    // ------- GETTERS -------
    public List<Object> getHtmlAttributions() {
        return htmlAttributions;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public List<Result> getResults() {
        return results;
    }

    public String getStatus() {
        return status;
    }


    // ------- SETTERS -------
    public void setHtmlAttributions(List<Object> htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
