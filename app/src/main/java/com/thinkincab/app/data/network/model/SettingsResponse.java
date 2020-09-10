package com.thinkincab.app.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SettingsResponse {

    @SerializedName("serviceTypes")
    @Expose
    private List<ServiceType> serviceTypes;
    @SerializedName("referral")
    @Expose
    private Referral referral;

    @SerializedName("night_start")
    @Expose
    private String night_start;

    @SerializedName("night_end")
    @Expose
    private String night_end;

    public String getNight_start() {
        return night_start;
    }

    public void setNight_start(String night_start) {
        this.night_start = night_start;
    }

    public String getNight_end() {
        return night_end;
    }

    public void setNight_end(String night_end) {
        this.night_end = night_end;
    }

    public List<ServiceType> getServiceTypes() {
        return serviceTypes;
    }

    public void setServiceTypes(List<ServiceType> serviceTypes) {
        this.serviceTypes = serviceTypes;
    }

    public Referral getReferral() {
        return referral;
    }

    public void setReferral(Referral referral) {
        this.referral = referral;
    }

}
