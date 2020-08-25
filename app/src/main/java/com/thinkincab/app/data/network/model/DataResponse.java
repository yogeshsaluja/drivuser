package com.thinkincab.app.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DataResponse {

    @SerializedName("data")
    @Expose
    private List<Datum> data = new ArrayList<>();
    @SerializedName("bids")
    @Expose
    private List<BidModel> bids = new ArrayList<>();


    @SerializedName("sos")
    @Expose
    private String sos;
    @SerializedName("cash")
    @Expose
    private int cash;
    @SerializedName("card")
    @Expose
    private int card;
    @SerializedName("currency")
    @Expose
    private String currency;

    @SerializedName("payumoney")
    @Expose
    private int payumoney;

    @SerializedName("paypal")
    @Expose
    private int paypal;

    @SerializedName("braintree")
    @Expose
    private int braintree;

    @SerializedName("paytm")
    @Expose
    private int paytm;

    @SerializedName("debit_machine")
    @Expose
    private int debit_machine;

    @SerializedName("voucher")
    @Expose
    private int voucher;

    @SerializedName("paypal_adaptive")
    @Expose
    private int paypal_adaptive;

    public int getPaypal_adaptive() {
        return paypal_adaptive;
    }

    public void setPaypal_adaptive(int paypal_adaptive) {
        this.paypal_adaptive = paypal_adaptive;
    }

    public int getPayumoney() {
        return payumoney;
    }

    public void setPayumoney(int payumoney) {
        this.payumoney = payumoney;
    }

    public int getPaypal() {
        return paypal;
    }

    public void setPaypal(int paypal) {
        this.paypal = paypal;
    }

    public int getBraintree() {
        return braintree;
    }

    public void setBraintree(int braintree) {
        this.braintree = braintree;
    }

    public int getPaytm() {
        return paytm;
    }

    public void setPaytm(int paytm) {
        this.paytm = paytm;
    }

    //TODO ALLAN - Alterações débito na máquina e voucher
    public int getDebitMachine() {
        return debit_machine;
    }

    public void setDebitMachine(int debit_machine) {
        this.debit_machine = debit_machine;
    }

    public int getVoucher() {
        return voucher;
    }

    public void setVoucher(int voucher) {
        this.voucher = voucher;
    }

    public DataResponse() {
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public String getSos() {
        return sos;
    }

    public void setSos(String sos) {
        this.sos = sos;
    }

    public int getCash() {
        return cash;
    }

    public void setCash(int cash) {
        this.cash = cash;
    }

    public int getCard() {
        return card;
    }

    public void setCard(int card) {
        this.card = card;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }


    public List<BidModel> getBids() {
        return bids;
    }

    public void setBids(List<BidModel> bids) {
        this.bids = bids;
    }
}
