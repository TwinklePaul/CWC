package com.team.NEBULA.cwc.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PoliceRetofitInstance {
    companion object {
        private var retrofit:Retrofit? = null
        private var BASE_URL:String = "https://maps.googleapis.com/"
        fun getPoliceStationListService():GetPoliceStationListService{

            if (retrofit==null){
                retrofit =Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }

            return retrofit!!.create(GetPoliceStationListService::class.java)
        }
    }
}