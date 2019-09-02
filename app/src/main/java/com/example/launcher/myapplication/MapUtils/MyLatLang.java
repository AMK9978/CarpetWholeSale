package com.example.launcher.myapplication.MapUtils;

import com.google.gson.annotations.SerializedName;

public class MyLatLang {
    @SerializedName("latitude")
    Double latitude;

    @SerializedName("longitude")
    Double longitude;

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "MyLatLang{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
