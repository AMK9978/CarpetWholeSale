package com.example.launcher.myapplication.MapUtils;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.ArrayList;


public class LocationModel {

    private LatLng lng;

    @SerializedName("name")
    private String name;

    @SerializedName("LatLang")
    private ArrayList<Double> LatLang;

    @SerializedName("des")
    private String description;

    @SerializedName("address")
    private String address;

//    @SerializedName("times")
//    private ArrayList<Station_Time> times;

    public ArrayList<Double> getLatLang() {
        return LatLang;
    }

    public void setLatLang(ArrayList<Double> latLang) {
        LatLang = latLang;
        setLng(new LatLng(latLang.get(0), latLang.get(1)));
    }

    public LatLng getLng() {
        return lng;
    }

    public void setLng(LatLng lng) {
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

//    public ArrayList<Station_Time> getTimes() {
//        return times;
//    }
//
//    public void setTimes(ArrayList<Station_Time> times) {
//        this.times = times;
//    }
}
