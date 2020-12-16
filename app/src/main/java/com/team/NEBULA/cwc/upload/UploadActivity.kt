package com.team.NEBULA.cwc.upload

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.appcompat.app.AlertDialog
import android.widget.Toast
import androidx.core.content.FileProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.team.NEBULA.cwc.HistoryActivity
import com.team.NEBULA.cwc.R
import com.team.NEBULA.cwc.data.CwcLoactionListener
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_upload.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class UploadActivity : AppCompatActivity() {

    val REQUEST_IMAGE_CAPTURE = 18
    val RESULT_LOAD_IMAGE = 12
    val LOCATION_CODE = 212
    lateinit var currentPhotoPath: String
    private val FETCH_IMAGE_CODE = 10252
    private var m_capturedImageUri: Uri? = null
    private lateinit var cwcLoactionListener: CwcLoactionListener
    private lateinit var locationManager: LocationManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)
        setSupportActionBar(toolbar)

        toolbar.setTitle(getString(R.string.app_name))
        var builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())


        clkImage.setOnClickListener {
            dispatchTakePictureIntent()
        }




    }

    override fun onResume() {
        super.onResume()
        checkLocationPermission();
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.homemenu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item!!.itemId) {
            R.id.hmMenu -> {
                var i = Intent(applicationContext,HistoryActivity::class.java)
                startActivity(i)
                return true
            }
            R.id.hmLog->{
                FirebaseAuth.getInstance().signOut()
                return true
            }

        }

        return super.onOptionsItemSelected(item)
    }
    private fun checkLocationPermission() {
        if(Build.VERSION.SDK_INT>=23){

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED ){

                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_CODE)
                return
            }
        }
        startService()
    }

    private fun startService(){
        if (!CwcLoactionListener.isRunning){
            cwcLoactionListener = CwcLoactionListener.getInstance()

            locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER,0,0f,cwcLoactionListener)
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0f,cwcLoactionListener)
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0f,cwcLoactionListener)
        }
    }


    private fun dispatchGalleryPictureIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMAGE)
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
            val intent = Intent(applicationContext,SendActivity::class.java)
            intent.putExtra("isCamera",true)
            intent.putExtra("showImg",m_capturedImageUri.toString())
            val compresedImg = Compressor(applicationContext).compressToFile(File(currentPhotoPath))
            m_capturedImageUri = Uri.fromFile(compresedImg)
            intent.putExtra("uriArray", m_capturedImageUri.toString())
            startActivity(intent)

        } else if (requestCode == RESULT_LOAD_IMAGE) {
            var imgList:ArrayList<String> = ArrayList()
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    Toast.makeText(this,"You did not select any picture",Toast.LENGTH_LONG).show()
                }

                val clipData = data!!.clipData
                if (clipData != null) { // handle multiple photo
                    for (i in 0 until clipData.itemCount) {
                        val uri = clipData.getItemAt(i).uri
                        imgList.add(uri.toString())
                    }
                } else { // handle single photo
                    val uri = data.data
                    imgList.add(uri.toString())
                }
                val intent = Intent(applicationContext,SendActivity::class.java)
                intent.putExtra("isCamera",true)
                intent.putExtra("stringArray", imgList)
                startActivity(intent)
            }
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        when (requestCode) {
            FETCH_IMAGE_CODE-> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dispatchGalleryPictureIntent()
                } else {
                    Toast.makeText(this, "Cannot acces to Gallery", Toast.LENGTH_LONG).show()
                }
            }
            LOCATION_CODE->{

                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this,"Accept this permission",Toast.LENGTH_LONG).show()
                }else{
                    startService()
                }
            }
            else ->{
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }

        }


    }

    override fun onStop() {
        super.onStop()
        locationManager.removeUpdates(cwcLoactionListener)
        CwcLoactionListener.isRunning = false
    }

}