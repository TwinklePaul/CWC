package com.team.NEBULA.cwc.service;

import com.team.NEBULA.cwc.numbermodel.PoliceStationNumber;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetPoliceStationNumberService {
    @GET("maps/api/place/details/json")
    Call<PoliceStationNumber> getNumbers(@Query(value = "placeid", encoded = true) String placeId,
                                         @Query("fields") String fields,
                                         @Query("key") String key);
}
