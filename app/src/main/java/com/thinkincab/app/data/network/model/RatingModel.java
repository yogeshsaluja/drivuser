package com.thinkincab.app.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RatingModel{
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("provider_id")
    @Expose
    private Integer providerId;

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Integer getProviderId() {
        return providerId;
    }

}
