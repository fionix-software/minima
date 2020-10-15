package net.fionix.minima

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class ActivityMain : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        // super
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // bottom navigation
        val bottomNav = findViewById<View>(R.id.navigation) as BottomNavigationView
        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_view -> {
                    supportFragmentManager.beginTransaction().replace(R.id.content, ActivityCourse()).commit()
                }
                R.id.navigation_list -> {
                    supportFragmentManager.beginTransaction().replace(R.id.content, ActivityTimetable()).commit()
                }
                R.id.navigation_setting -> {
                    supportFragmentManager.beginTransaction().replace(R.id.content, ActivitySettings()).commit()
                }
            }
            false
        }

        // open default
        supportFragmentManager.beginTransaction().replace(R.id.content, ActivityCourse()).commit()
    }
}