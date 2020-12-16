package com.team.NEBULA.cwc.upload

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.team.NEBULA.cwc.R
import android.location.LocationManager
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.FileProvider
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.team.NEBULA.cwc.Adapter.ImageAdapter
import com.team.NEBULA.cwc.data.CwcLoactionListener
import com.team.NEBULA.cwc.data.SendDataDetails
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_send.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class SendActivity : AppCompatActivity() {

    private lateinit var arrayList:ArrayList<String>
    private lateinit var imageAdapter:ImageAdapter
    private lateinit var imageUri:String
    private lateinit var viewImgUri:String
    private lateinit var recyclerView: androidx.recyclerview.widget.RecyclerView
    private lateinit var recylerViewLayoutManager: androidx.recyclerview.widget.RecyclerView.LayoutManager
    var auth: FirebaseAuth? = null
    private lateinit var mFirebaseDatabase: FirebaseDatabase
    private lateinit var mRef: DatabaseReference
    private lateinit var mFirebaseStorage: FirebaseStorage
    private lateinit var mStorageRef: StorageReference
    private lateinit var cwcLoactionListener: CwcLoactionListener
    private lateinit var locationManager: LocationManager
    private lateinit var flexLayoutManager:FlexboxLayoutManager
    private lateinit var imgArrList:ArrayList<String>
    lateinit var currentPhotoPath: String
    private val FETCH_IMAGE_CODE = 10252
    private var m_capturedImageUri: Uri? = null

    val REQUEST_IMAGE_CAPTURE = 1



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send)
        setSupportActionBar(toolbarSend)
        toolbarSend.setTitle(getString(R.string.app_name))
        var builder = StrictMode.VmPolicy.Builder()

        StrictMode.setVmPolicy(builder.build())
        startService()
        auth = FirebaseAuth.getInstance()
        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mRef = mFirebaseDatabase.reference.child("user").child(auth!!.currentUser!!.displayName.toString().trim())
        mFirebaseStorage  = FirebaseStorage.getInstance()
        mStorageRef = mFirebaseStorage.reference.child("realtime_images")


        imageUri = intent.getStringExtra("uriArray")
        viewImgUri = intent.getStringExtra("showImg")
        arrayList = ArrayList()
        imgArrList = ArrayList()
        arrayList.add(imageUri)
        imgArrList.add(viewImgUri)
        flexLayoutManager = FlexboxLayoutManager(this)
        flexLayoutManager.flexDirection = FlexDirection.ROW
        flexLayoutManager.justifyContent = JustifyContent.FLEX_START

        cwcLoactionListener = CwcLoactionListener.getInstance()
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        imageAdapter = ImageAdapter(imgArrList,applicationContext)


        recyclerView = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recView).apply {
            layoutManager = flexLayoutManager
            adapter = imageAdapter
        }
        /* imageView.setImageBitmap(_bitmap)
         imageView.scaleType = ImageView.ScaleType.FIT_XY*/


        sendInfo.setOnClickListener {
            var i = Intent(applicationContext,AddExtraInfoActivity::class.java)
            SendDataDetails.imageUrlsArray = arrayList
            startActivity(i)
            finish()
        }
    }

    private fun startService(){
        if (!CwcLoactionListener.isRunning){

            locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER,0,0f,cwcLoactionListener)
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0f,cwcLoactionListener)
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0f,cwcLoactionListener)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mainmenu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item!!.itemId) {
            R.id.addMoreImg -> {
                dispatchTakePictureIntent()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_DCIM)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }



    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.team.NEBULA.cwc.fileprovider",
                        it
                    )

                    val imageFile = File(currentPhotoPath)
                    m_capturedImageUri = Uri.fromFile(imageFile)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, m_capturedImageUri)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            imgArrList.add(m_capturedImageUri.toString())
            val compresedImg = Compressor(applicationContext).compressToFile(File(currentPhotoPath))
            m_capturedImageUri = Uri.fromFile(compresedImg)
            arrayList.add(m_capturedImageUri.toString())
            imageAdapter.notifyDataSetChanged()

        }
    }

    /*class LocationFetchTask(private var activity: SendActivity):AsyncTask<String,String,String>(){


        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

        }

        override fun onPreExecute() {
            super.onPreExecute()
            activity.loadingBar.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg params: String?): String {

            if (CwcLoactionListener.mylocation!=null){
                activity.addDataToFirebase(CwcLoactionListener.mylocation!!.latitude.toString(),CwcLoactionListener.mylocation!!.longitude.toString())
            }else{
                while (true){
                    if (CwcLoactionListener.mylocation!=null){
                        activity.addDataToFirebase(CwcLoactionListener.mylocation!!.latitude.toString(),CwcLoactionListener.mylocation!!.longitude.toString())
                        Log.d("Tracing","************************ Location found")
                        break
                    }
                    Log.d("Tracing","************************ Location Not found"+CwcLoactionListener.mylocation+CwcLoactionListener.isRunning)
                    Thread.sleep(3000)
                }
            }

            return  null.toString()
        }
    }
*/

}