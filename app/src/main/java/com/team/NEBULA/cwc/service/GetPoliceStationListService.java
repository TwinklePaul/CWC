package com.team.NEBULA.cwc.service;

import com.team.NEBULA.cwc.model.PoliceStationLists;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetPoliceStationListService {
    @GET("maps/api/place/nearbysearch/json")
    Call<PoliceStationLists> getPoliceStations(@Query(value = "location",encoded = true) String location,
                                               @Query("radius") String radius,
                                               @Query("type") String type,
                                               @Query("key") String key);
}
