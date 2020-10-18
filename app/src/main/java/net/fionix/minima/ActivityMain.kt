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
                    supportActionBar!!.title = "Course List"
                    supportFragmentManager.beginTransaction().replace(R.id.content, ActivityCourse()).commit()
                }
                R.id.navigation_list -> {
                    supportActionBar!!.title = "Timetable List"
                    supportFragmentManager.beginTransaction().replace(R.id.content, ActivityTimetableList()).commit()
                }
                R.id.navigation_table -> {
                    supportActionBar!!.title = "Timetable table"
                    supportFragmentManager.beginTransaction().replace(R.id.content, ActivityTimetableTable()).commit()
                }
                R.id.navigation_setting -> {
                    supportActionBar!!.title = "Settings"
                    supportFragmentManager.beginTransaction().replace(R.id.content, ActivitySettings()).commit()
                }
            }
            true
        }

        // open default
        supportActionBar!!.title = "Course List"
        supportFragmentManager.beginTransaction().replace(R.id.content, ActivityCourse()).commit()
    }
}