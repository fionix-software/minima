package net.fionix.minima

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity

class ActivitySplash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // get variables
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val first = sharedPreferences.getBoolean("first", true)

        // splash screen
        val splashThread: Thread = object : Thread() {
            override fun run() {
                try {
                    sleep(1000)

                    // redirect
                    if (first) {
                        val intent = Intent(applicationContext, ActivityIntro::class.java)
                        startActivity(intent)
                    } else {
                        val intent = Intent(applicationContext, ActivityMain::class.java)
                        startActivity(intent)
                    }
                    finish()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
        splashThread.start()
    }
}