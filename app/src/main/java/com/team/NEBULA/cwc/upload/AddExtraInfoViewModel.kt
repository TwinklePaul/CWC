package com.team.NEBULA.cwc.upload

import android.content.Context
import android.location.Geocoder
import android.location.LocationManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.work.*
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.team.NEBULA.cwc.data.LocationDetailsCWC
import com.team.NEBULA.cwc.data.SendFileDetails
import com.team.NEBULA.cwc.workers.UploadDataWorker
import java.text.SimpleDateFormat
import java.util.*

class AddExtraInfoViewModel:ViewModel() {

    private lateinit var mFirebaseDatabase: FirebaseDatabase
    private lateinit var mRef: DatabaseReference
    private lateinit var mFirebaseStorage: FirebaseStorage
    private lateinit var mStorageRef: StorageReference
    private lateinit var locationManager: LocationManager
    private val workManager: WorkManager = WorkManager.getInstance()

    internal fun startUploadInfo(userName:String,userEmail:String,timeCwc:String,imageUrlsArrayList: ArrayList<String>?,info:String?=null){
        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mRef = mFirebaseDatabase.reference.child("user").child(userEmail).child(userName).child(timeCwc)
        mFirebaseStorage  = FirebaseStorage.getInstance()
        mStorageRef = mFirebaseStorage.reference.child("realtime_images")
        val sdf = SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z")
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val newWork = OneTimeWorkRequestBuilder<UploadDataWorker>()
            .setInputData(createDataForUpload(userName,userEmail,timeCwc,
                imageUrlsArrayList,LocationDetailsCWC.lat!!.toDouble(),LocationDetailsCWC.long!!.toDouble(),
                (sdf.format(Date(timeCwc.toLong()*1000))).toString(),info))
            .setConstraints(constraints)
            .build()

        workManager.enqueue(newWork)

    }

    private fun createDataForUpload(userName:String,userEmail:String,timeCwc:String,
                                    imageUrlsArrayList: ArrayList<String>?,lat:Double,
                                    longti:Double,humanReadableTime:String,info:String?=null):Data{
        var builder = Data.Builder()
        builder.putStringArray(TAG_IMAGEURLARRAYLIST,imageUrlsArrayList!!.toTypedArray())
        builder.putString(TAG_USERNAME,userName)
        builder.putString(TAG_USEREMAIL,userEmail)
        builder.putString(TAG_HUMANREADABLETIME,humanReadableTime)
        builder.putString(KEY_LATITUDE,lat.toString())
        builder.putString(KEY_LONGITUDE,longti.toString())
        builder.putString(TAG_TIMECWC,timeCwc)
        builder.putString(TAG_INFO,info)
        return builder.build()
    }

}