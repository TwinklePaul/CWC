package com.team.NEBULA.cwc.success

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import com.team.NEBULA.cwc.R
import com.team.NEBULA.cwc.cwcmap.CWCMapsActivity

import kotlinx.android.synthetic.main.activity_done.*

class DoneActivity : AppCompatActivity() {

    private val SPLASH_SCREEN_DEALY:Long = 1000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_done)
        setSupportActionBar(toolbar)
        toolbar.title = "CWC"
        Handler().postDelayed({
            val mainIntent = Intent(this@DoneActivity, CWCMapsActivity::class.java)
            this@DoneActivity.startActivity(mainIntent)
            this@DoneActivity.finish()
        }, SPLASH_SCREEN_DEALY)

    }

}
