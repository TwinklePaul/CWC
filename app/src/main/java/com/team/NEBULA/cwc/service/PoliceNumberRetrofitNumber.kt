package com.team.NEBULA.cwc.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PoliceNumberRetrofitNumber {
    companion object {
        private var retrofit:Retrofit? = null
        private val BASE_URL = "https://maps.googleapis.com/"

        fun getPoliceStationNumberService():GetPoliceStationNumberService{

            if (retrofit==null){
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }

            return retrofit!!.create(GetPoliceStationNumberService::class.java)
        }
    }
}