package com.example.launcher.myapplication.API;

import com.example.launcher.myapplication.MapUtils.LocationModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIInterface {
    @GET("bins/pq7mn")
    Call<List<LocationModel>> getLocations();


}
