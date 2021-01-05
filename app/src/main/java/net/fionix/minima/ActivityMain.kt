package net.fionix.minima

import android.app.AlertDialog
import android.graphics.Bitmap
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.github.tlaabs.timetableview.TimetableView
import com.google.android.material.bottomnavigation.BottomNavigationView
import net.fionix.minima.util.OnButtonClickDismissAlertDialog
import net.fionix.minima.util.UtilBitmap
import java.io.File
import java.io.FileOutputStream


class ActivityMain : AppCompatActivity() {

    private var currentBottomNavItem: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {

        // super
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // bottom navigation
        val bottomNav = findViewById<View>(R.id.navigation) as BottomNavigationView
        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_view -> {
                    supportActionBar!!.title = getString(R.string.title_course_list)
                    supportFragmentManager.beginTransaction().replace(R.id.content, ActivityCourse()).commit()
                    currentBottomNavItem = R.id.navigation_view
                }
                R.id.navigation_list -> {
                    supportActionBar!!.title = getString(R.string.title_timetable_list)
                    supportFragmentManager.beginTransaction().replace(R.id.content, ActivityTimetableList()).commit()
                    currentBottomNavItem = R.id.navigation_list
                }
                R.id.navigation_table -> {
                    supportActionBar!!.title = getString(R.string.title_timetable_table)
                    supportFragmentManager.beginTransaction().replace(R.id.content, ActivityTimetableTable()).commit()
                    currentBottomNavItem = R.id.navigation_table
                }
                R.id.navigation_setting -> {
                    supportActionBar!!.title = getString(R.string.title_setting)
                    supportFragmentManager.beginTransaction().replace(R.id.content, ActivitySettings()).commit()
                    currentBottomNavItem = R.id.navigation_setting
                }
            }

            // refresh options menu
            invalidateOptionsMenu()

            // return
            true
        }

        // open default
        supportActionBar!!.title = getString(R.string.title_course_list)
        supportFragmentManager.beginTransaction().replace(R.id.content, ActivityCourse()).commit()
        currentBottomNavItem = R.id.navigation_view
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {

        // check for null
        if (menu == null) {
            return false
        }

        // unhide when at navigation table
        when (currentBottomNavItem) {
            R.id.navigation_table -> {
                menu.findItem(R.id.share_table).isVisible = true;
            }
            else -> {
                menu.findItem(R.id.share_table).isVisible = false;
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.info -> {

                // create info dialog
                val infoDialog: AlertDialog.Builder = AlertDialog.Builder(this)
                infoDialog.setNegativeButton(getString(R.string.dialog_button_dismiss), OnButtonClickDismissAlertDialog {
                    // noop
                })

                // change content of info menu button accordingly
                when (currentBottomNavItem) {
                    R.id.navigation_view -> {
                        infoDialog.setMessage(getString(R.string.dialog_info_course_list))
                    }
                    R.id.navigation_list -> {
                        infoDialog.setMessage(getString(R.string.dialog_info_timetable_list))
                    }
                    R.id.navigation_table -> {
                        infoDialog.setMessage(getString(R.string.dialog_info_timetable_table))
                    }
                    R.id.navigation_setting -> {
                        infoDialog.setMessage(getString(R.string.dialog_info_settings))
                    }
                }

                // show
                infoDialog.show()
            }

            R.id.share_table -> {

                // change content of info menu button accordingly
                when (currentBottomNavItem) {
                    R.id.navigation_table -> {
                        val fragmentView: View? = supportFragmentManager.fragments.last().view
                        if (fragmentView != null) {
//                            val timetable: TimetableView = fragmentView.findViewById(R.id.timetable)
//                            val bitmap: Bitmap? = UtilBitmap.renderFromView(timetable)
//                            if (bitmap != null) {
//                                saveImage(bitmap)
//                            }
                        }
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveImage(bitmap: Bitmap) {
        val dir: File? = getExternalFilesDir(null)
        if (dir != null) {
            val file = File(dir, "minima.png")
            val fOut = FileOutputStream(file)

            bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut)
            fOut.flush()
            fOut.close()
        }
    }
}