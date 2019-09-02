package com.example.launcher.myapplication.Models;


public class Report {
    private String date;
    private long amount = 0;
    private int carpet_id;
    private int merchant_id;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public int getCarpet_id() {
        return carpet_id;
    }

    public void setCarpet_id(int carpet_id) {
        this.carpet_id = carpet_id;
    }

    public int getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(int merchant_id) {
        this.merchant_id = merchant_id;
    }
}
