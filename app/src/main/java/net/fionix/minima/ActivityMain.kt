package net.fionix.minima

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import net.fionix.minima.util.OnButtonClickDismissAlertDialog
import java.io.File
import java.io.FileOutputStream


class ActivityMain : AppCompatActivity() {

    private var currentBottomNavItem: Int = R.id.navigation_view

    override fun onCreate(savedInstanceState: Bundle?) {

        // super
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // bottom navigation
        val bottomNav = findViewById<BottomNavigationView>(R.id.navigation)
        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_view -> {
                    supportFragmentManager.beginTransaction().replace(R.id.content, ActivityCourse()).commit()
                    currentBottomNavItem = R.id.navigation_view
                }
                R.id.navigation_list -> {
                    supportFragmentManager.beginTransaction().replace(R.id.content, ActivityTimetableList()).commit()
                    currentBottomNavItem = R.id.navigation_list
                }
                R.id.navigation_table -> {
                    supportFragmentManager.beginTransaction().replace(R.id.content, ActivityTimetableTable()).commit()
                    currentBottomNavItem = R.id.navigation_table
                }
                R.id.navigation_setting -> {
                    supportFragmentManager.beginTransaction().replace(R.id.content, ActivitySettings()).commit()
                    currentBottomNavItem = R.id.navigation_setting
                }
            }

            // refresh options menu
            invalidateOptionsMenu()

            // return
            true
        }

        // opens course fragment if not set
        if (supportFragmentManager.findFragmentById(R.id.content) == null) {
            supportFragmentManager.beginTransaction().replace(R.id.content, ActivityCourse()).commit()
        }
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
                menu.findItem(R.id.share_table).isVisible = true
            }
            else -> {
                menu.findItem(R.id.share_table).isVisible = false
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

                        // get fragment view
                        val fragmentView: View? = supportFragmentManager.fragments.last().view
                        if (fragmentView != null) {

                            // generate bitmap and share
                            val bitmap = generateBitmap(fragmentView.findViewById(R.id.scrollView))
                            shareBitmap(this, bitmap)
                        }
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun generateBitmap(view: ScrollView): Bitmap {

        // draw canvas according to view
        val bitmap = Bitmap.createBitmap(view.getChildAt(0).width, view.getChildAt(0).height, Bitmap.Config.ARGB_8888)
        view.draw(Canvas(bitmap))
        return bitmap

    }

    private fun shareBitmap(context: Context, bitmap: Bitmap) {

        // save bitmap file temporary in application data dir and get uri
        val file = File(filesDir, getString(R.string.app_name) + ".png")
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, FileOutputStream(file))
        val uri = FileProvider.getUriForFile(context, context.applicationContext.packageName.toString() + ".provider", file)

        // init and open share intent
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/png"
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        // permission
        val intentChooser = Intent.createChooser(intent, "Send timetable image")
        val resInfoList = this.packageManager.queryIntentActivities(intentChooser, PackageManager.MATCH_DEFAULT_ONLY)
        for (resolveInfo in resInfoList) {
            val packageName = resolveInfo.activityInfo.packageName
            grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(intentChooser)

    }
}