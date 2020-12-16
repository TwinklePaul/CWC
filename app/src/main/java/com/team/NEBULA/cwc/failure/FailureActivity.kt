package com.team.NEBULA.cwc.failure

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.team.NEBULA.cwc.R

class FailureActivity : AppCompatActivity() {

    private val SPLASH_SCREEN_DEALY:Long = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_failure)
        Handler().postDelayed({
            this@FailureActivity.finish()
        }, SPLASH_SCREEN_DEALY)
    }
}
