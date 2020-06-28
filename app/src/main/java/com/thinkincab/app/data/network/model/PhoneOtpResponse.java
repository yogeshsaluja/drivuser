package com.thinkincab.app.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PhoneOtpResponse {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("verified")
    @Expose
    private Boolean verified;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("otp")
    @Expose
    private Integer otp;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getOtp() {
        return otp;
    }

    public void setOtp(Integer otp) {
        this.otp = otp;
    }
}
