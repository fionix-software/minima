package net.fionix.minima

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import net.fionix.minima.ActivityIntro

class ActivityIntro : AppCompatActivity() {
    var slideMax = 5
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        val iconImage = findViewById<ImageView>(R.id.imageView1)
        val nextBtn = findViewById<Button>(R.id.button1)
        val introTxt = findViewById<TextView>(R.id.textView1)
        nextBtn.setOnClickListener {
            slideMax--
            if (slideMax == 4) {
                iconImage.setImageResource(R.drawable.ic_github)
                introTxt.setText(R.string.intro2)
            } else if (slideMax == 3) {
                iconImage.setImageResource(R.drawable.ic_code_branch)
                introTxt.setText(R.string.intro3)
            } else if (slideMax == 2) {
                iconImage.setImageResource(R.drawable.ic_handshake)
                introTxt.setText(R.string.intro4)
            } else if (slideMax == 1) {
                iconImage.setImageResource(R.drawable.ic_smile_wink)
                introTxt.setText(R.string.intro5)
                nextBtn.setText(R.string.start)
            } else {
                val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this@ActivityIntro)
                val editor = sharedPreferences.edit()
                editor.putBoolean("first", false)
                editor.commit()
                val intent = Intent(applicationContext, ActivityMain::class.java)
                startActivity(intent)
            }
        }
    }
}