package com.team.NEBULA.cwc.data

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.team.NEBULA.cwc.R
import kotlinx.android.synthetic.main.activity_send.view.*

class CwcLoactionListener private constructor() :LocationListener{

   companion object {
       var mylocation:Location? = null
       var isRunning:Boolean = false
       fun getInstance():CwcLoactionListener{
           val cwcLoactionListener = CwcLoactionListener()
           return cwcLoactionListener
       }
   }
    init {
        isRunning = true
    }



    override fun onLocationChanged(location: Location?) {
        mylocation = location!!
        Log.d("Hello","*************************************************** "+ mylocation.toString())
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

    }

    override fun onProviderEnabled(provider: String?) {
        Log.d("Hello","*************************************************** "+ mylocation.toString())
    }


    override fun onProviderDisabled(provider: String?) {

    }


}