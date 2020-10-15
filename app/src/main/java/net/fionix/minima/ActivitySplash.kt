package net.fionix.minima

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class ActivitySplash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        // super
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // wait for 1s and continue
        Handler((Looper.getMainLooper())).postDelayed({
            startActivity(Intent(this, ActivityMain::class.java))
            finish()
        }, 1000)
    }
}