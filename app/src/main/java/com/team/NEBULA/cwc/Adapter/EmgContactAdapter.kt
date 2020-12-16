package com.team.NEBULA.cwc.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.team.NEBULA.cwc.R
import com.team.NEBULA.cwc.data.ContactDetails
import com.team.NEBULA.cwc.model.Result
import com.team.NEBULA.cwc.numbermodel.PoliceStationNumber
import com.team.NEBULA.cwc.service.PoliceNumberRetrofitNumber
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EmgContactAdapter(val policeStations:ArrayList<ContactDetails>,val context: Context) : RecyclerView.Adapter<EmgContactAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.contact_list,parent,false)

        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return policeStations.size
    }

    override fun onBindViewHolder(parent: MyViewHolder, position: Int) {
        parent.nameView!!.text = policeStations.get(position).placeName
        parent.numberView!!.text = policeStations.get(position).placeNumber
        parent.phnBu!!.setOnClickListener {

        }
    }

    class MyViewHolder(view: View) :RecyclerView.ViewHolder(view){

        var nameView:TextView? = null
        var numberView:TextView? = null
        var phnBu:ImageButton? = null

        init {
            nameView = view.findViewById(R.id.placeName)
            numberView = view.findViewById(R.id.placeNumber)
            phnBu = view.findViewById(R.id.phnButton)
        }
    }



}