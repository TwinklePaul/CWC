package com.team.NEBULA.cwc.workers

import android.content.Context
import android.location.Geocoder
import android.net.Uri
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.team.NEBULA.cwc.data.SendFileDetails
import com.team.NEBULA.cwc.upload.*
import java.util.*

class UploadDataWorker(contxt:Context,params:WorkerParameters):Worker(contxt,params) {

    private lateinit var mFirebaseDatabase: FirebaseDatabase
    private lateinit var mRef: DatabaseReference
    private lateinit var mFirebaseStorage: FirebaseStorage
    private lateinit var mStorageRef: StorageReference
    private val TAG by lazy {UploadDataWorker::class.java.simpleName}
    override fun doWork(): Result {

        val imageUrlList = inputData.getStringArray(TAG_IMAGEURLARRAYLIST)
        val userName = inputData.getString(TAG_USERNAME)
        val userEmail = inputData.getString(TAG_USEREMAIL)
        val timeStamp = inputData.getString(TAG_TIMECWC)
        val humanReadableTimeFormat = inputData.getString(TAG_HUMANREADABLETIME)
        val lat = inputData.getString(KEY_LATITUDE)
        val longi = inputData.getString(KEY_LONGITUDE)
        val info = inputData.getString(TAG_INFO)

        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mRef = mFirebaseDatabase.reference.child("user").child(userEmail!!).child(userName!!)
        mFirebaseStorage  = FirebaseStorage.getInstance()
        mStorageRef = mFirebaseStorage.reference.child("realtime_images")

        return try {

            for (i in 0 until (imageUrlList!!.size)) {

                val imageRef = mStorageRef.child(userName + humanReadableTimeFormat + i)
                imageRef.putFile(Uri.parse(imageUrlList.get(i))).addOnSuccessListener { taskSnapShot ->
                    var downloadUrl: Task<Uri> = taskSnapShot.metadata!!.reference!!.downloadUrl

                    while (!downloadUrl.isSuccessful);

                    var sendFileDetails = SendFileDetails(
                        lat.toString(),
                        longi.toString(),
                        downloadUrl.result.toString(),humanReadableTimeFormat,timeStamp
                    )
                    mRef.push().setValue(sendFileDetails)
                }.addOnFailureListener {
                    Log.d("jobStart", "*********************************************** " + it.message)
                }
            }

            if (info != null) {
                mRef.child("Info").setValue(info)
            }
            makeStatusNotification("Upload Complete",applicationContext)
            Result.success()
        }catch (e:IllegalStateException){
            Log.d(TAG,e.toString())
            Result.failure()
        }
    }
}