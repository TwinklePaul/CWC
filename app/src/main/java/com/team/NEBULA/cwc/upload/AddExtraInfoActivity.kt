package com.team.NEBULA.cwc.upload

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import com.team.NEBULA.cwc.R
import com.team.NEBULA.cwc.data.CwcLoactionListener
import com.team.NEBULA.cwc.data.SendDataDetails
import com.team.NEBULA.cwc.success.DoneActivity
import kotlinx.android.synthetic.main.activity_add_extra_info.*
import android.net.ConnectivityManager
import android.text.InputFilter
import androidx.lifecycle.ViewModelProviders
import com.team.NEBULA.cwc.data.LocationDetailsCWC
import com.team.NEBULA.cwc.failure.FailureActivity


class AddExtraInfoActivity : AppCompatActivity() {

    private lateinit var locationManager: LocationManager
    private lateinit var cwcLoactionListener: CwcLoactionListener
    private val DEFAULT_MSG_LENGTH_LIMIT = 500
    private lateinit var viewModel: AddExtraInfoViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_extra_info)
        setSupportActionBar(toolbarExtra)
        toolbarExtra.setTitle(resources.getString(R.string.app_name))

        viewModel = ViewModelProviders.of(this).get(AddExtraInfoViewModel::class.java)

        cwcLoactionListener = CwcLoactionListener.getInstance()
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        writeSms.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().trim({ it <= ' ' }).length > 0) {
                    sendMessage.isEnabled = true
                } else {
                    sendMessage.isEnabled = false
                }
            }
        })

        writeSms.setFilters(arrayOf<InputFilter>(InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)))

        sendMessage.setOnClickListener {


            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){

                if (isConnected()) {
                    startCwcService(writeSms.text.toString())
                    var i = Intent(applicationContext, DoneActivity::class.java)
                    startActivity(i)
                    finish()
                }else{
                    startCwcService(writeSms.text.toString())
                    var i = Intent(applicationContext, FailureActivity::class.java)
                    startActivity(i)
                    finish()
                }
            }else{
                var alert = AlertDialog.Builder(this)
                alert.setTitle("Warning")
                    .setMessage("Make sure the GPS is enable")
                    .setPositiveButton("ok") { dialog, which ->
                        dialog.dismiss()
                    }.create().show()
            }

        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.extrainfomenu,menu)
        return true
    }




    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item!!.itemId) {
            R.id.extraInfo -> {
                if (isConnected()) {
                    startCwcService()
                    var i = Intent(applicationContext, DoneActivity::class.java)
                    startActivity(i)
                    finish()
                }else{
                    startCwcService()
                    var i = Intent(applicationContext, FailureActivity::class.java)
                    startActivity(i)
                    finish()
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun isConnected():Boolean{
        val cm = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null
    }

    private fun startCwcService(info:String?=null){
        if (CwcLoactionListener.mylocation == null) {
            LocationDetailsCWC.lat =
                locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).latitude.toString()
            LocationDetailsCWC.long =
                locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).longitude.toString()
        } else {
            LocationDetailsCWC.lat = CwcLoactionListener.mylocation!!.latitude.toString()
            LocationDetailsCWC.long = CwcLoactionListener.mylocation!!.longitude.toString()
        }
        val unixTime = System.currentTimeMillis() / 1000
        viewModel.startUploadInfo(SendDataDetails.userName.toString(),
            SendDataDetails.userEmail.toString(),
            unixTime.toString(),
            SendDataDetails.imageUrlsArray!!,info)
    }




}
