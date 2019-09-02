package com.example.launcher.myapplication.MapUtils;

import com.google.gson.annotations.SerializedName;

public class Station_Time {

    @SerializedName("start")
    private String start;

    @SerializedName("end")
    private String end;

    public Station_Time(String start, String end) {
        this.start = start;
        this.end = end;
    }

    public String getEnd() {
        return end;
    }

    public String getStart() {
        return start;
    }
}
