package com.team.NEBULA.cwc

import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.team.NEBULA.cwc.Adapter.ImgHisAdapter
import com.team.NEBULA.cwc.data.SendDataDetails
import com.team.NEBULA.cwc.data.SendFileDetails
import kotlinx.android.synthetic.main.activity_history.*


class HistoryActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var imgArrayList: ArrayList<SendFileDetails>
    private lateinit var mFirebaseDatabase: FirebaseDatabase
    private lateinit var mRef: DatabaseReference
    private lateinit var mFirebaseStorage: FirebaseStorage
    private lateinit var mStorageRef: StorageReference
    private lateinit var locationManager: LocationManager
    private lateinit var mChildEventListener: ChildEventListener
    private lateinit var imageHistAdapter:ImgHisAdapter
    private lateinit var recyclerView: RecyclerView

    private val TAG by lazy {HistoryActivity::class.java.simpleName}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mRef = mFirebaseDatabase.reference.child("user").child(SendDataDetails.userEmail.toString()).child(SendDataDetails.userName.toString())
        addDataBaseListener()

        imgArrayList = ArrayList()
        imageHistAdapter = ImgHisAdapter(imgArrayList,applicationContext)
        recyclerView = findViewById<RecyclerView>(R.id.recViewHis).apply {
            layoutManager = GridLayoutManager(applicationContext,1)
            itemAnimator = DefaultItemAnimator()
            adapter = imageHistAdapter
        }
    }

    fun addDataBaseListener(){
        mChildEventListener = object : ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
                val imagesHis = dataSnapshot.getValue(SendFileDetails::class.java
                ) as SendFileDetails
                imgArrayList.add(imagesHis)
                if (imgArrayList.size>0){
                    loadingBar.visibility = View.GONE
                }
                imageHistAdapter.notifyDataSetChanged()
                Log.d("heyy",imgArrayList.size.toString())
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }

        }
        mRef.addChildEventListener(mChildEventListener)
    }

    override fun onPause() {
        super.onPause()
        mRef.removeEventListener(mChildEventListener)
    }
}
