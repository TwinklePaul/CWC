package com.team.NEBULA.cwc.cwcmap

import android.content.Context
import android.net.ConnectivityManager
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.team.NEBULA.cwc.Adapter.EmgContactAdapter
import com.team.NEBULA.cwc.R
import com.team.NEBULA.cwc.data.ContactDetails
import com.team.NEBULA.cwc.data.LocationDetailsCWC
import com.team.NEBULA.cwc.model.PoliceStationLists
import com.team.NEBULA.cwc.model.Result
import com.team.NEBULA.cwc.numbermodel.PoliceStationNumber
import com.team.NEBULA.cwc.service.PoliceNumberRetrofitNumber
import com.team.NEBULA.cwc.service.PoliceRetofitInstance
import kotlinx.android.synthetic.main.activity_cwcmaps.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CWCMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var policeStations:ArrayList<Result>
    private lateinit var recyclerView: RecyclerView
    private lateinit var contactDetails: ArrayList<ContactDetails>
    private var myTask:AsyncTask<String,String,String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cwcmaps)
        setSupportActionBar(toolbarMaps)
        toolbarMaps.title = "CWC"
        policeStations = ArrayList()
        contactDetails = ArrayList()
        getPoliceStationDetails()
        var mBottomSheet = BottomSheetBehavior.from(bottom_sheet)
        mBottomSheet.isHideable = false
        mBottomSheet.peekHeight = 300

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    private fun getPoliceStationDetails() {
        var loc = LocationDetailsCWC.lat!!.toDouble().toString() + "," +LocationDetailsCWC.long!!.toDouble().toString()
        Log.d("Hello",loc+"*****************************************")
        var policeStationListService = PoliceRetofitInstance.getPoliceStationListService()
        var policeStationResponce:Call<PoliceStationLists> =
            policeStationListService.getPoliceStations(loc,
                "5000",
                "police",resources.getString(R.string.google_maps_key))

        policeStationResponce.enqueue(object :Callback<PoliceStationLists>{
            override fun onResponse(call: Call<PoliceStationLists>, response: Response<PoliceStationLists>) {
                var body:PoliceStationLists = response.body()!!
                if (body.results != null){
                    policeStations = body.results as ArrayList<Result>
                    showOnRecyclerView()
                }
            }
            override fun onFailure(call: Call<PoliceStationLists>, t: Throwable) {

            }

        })
    }

    private fun showOnRecyclerView() {

        if (isConnected()) {

            if (policeStations.size > 5) {
                policeStations.subList(5, policeStations.size).clear()
            }

            val emgContactAdapter = EmgContactAdapter(contactDetails, applicationContext)
            recyclerView = findViewById<RecyclerView>(R.id.emgView).apply {
                layoutManager = LinearLayoutManager(applicationContext)
                adapter = emgContactAdapter
            }

            myTask = Mytasks(applicationContext, policeStations, contactDetails, emgContactAdapter)
            myTask!!.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        }
    }

    private fun isConnected():Boolean{
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(LocationDetailsCWC.lat!!.toDouble(), LocationDetailsCWC.long!!.toDouble())
        mMap.addMarker(MarkerOptions().position(sydney).title("Your Place"))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney,17f))

    }

    class Mytasks(val context: Context, val policeStations:ArrayList<Result>, val contactDetails: ArrayList<ContactDetails>, val emgContactAdapter: EmgContactAdapter):AsyncTask<String,String,String>(){
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            Log.d("Started","************************************* Stopedd")
        }

        override fun onPreExecute() {
            super.onPreExecute()
            Log.d("Started","************************************* Stared")
        }

        override fun doInBackground(vararg params: String?): String {
            var policeNumberInstance = PoliceNumberRetrofitNumber.getPoliceStationNumberService()
                for (i in 0 until policeStations.size) {
                    var stationName = policeStations.get(i).name
                    Log.d("Hello","*****************************"+policeStations.size)
                    var policeStationNumberService: Call<PoliceStationNumber> =
                        policeNumberInstance.getNumbers(
                            policeStations.get(i).placeId,
                            "formatted_phone_number",
                             context.resources.getString(R.string.google_maps_key)
                        )

                    policeStationNumberService.enqueue(object : Callback<PoliceStationNumber> {
                        override fun onFailure(call: Call<PoliceStationNumber>, t: Throwable) {
                            Log.d("onFAilNumber", "*************************" + t.message)
                        }

                        override fun onResponse(call: Call<PoliceStationNumber>, response: Response<PoliceStationNumber>) {
                            var body: PoliceStationNumber = response.body()!!

                            if (body.result != null) {
                                Log.d("Hell","***************************"+body.result.formattedPhoneNumber)
                                contactDetails.add(ContactDetails(stationName,body.result.formattedPhoneNumber))
                            }
                        }
                    })
                    publishProgress()
                    Thread.sleep(2000)
                }


            return null.toString()
        }

        override fun onProgressUpdate(vararg values: String?) {
            super.onProgressUpdate(*values)
            emgContactAdapter.notifyDataSetChanged()
        }
    }


}
