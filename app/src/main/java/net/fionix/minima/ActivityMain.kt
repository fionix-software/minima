package net.fionix.minima

import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class ActivityMain : AppCompatActivity() {
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val manager = supportFragmentManager
        when (item.itemId) {
            R.id.navigation_view -> {
                manager.beginTransaction().replace(R.id.content, ActivityCourse()).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_list -> {
                manager.beginTransaction().replace(R.id.content, ActivityTimetable()).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_setting -> {
                manager.beginTransaction().replace(R.id.content, ActivitySettings()).commit()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // app shortcut
        var shortcutManager: ShortcutManager? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                shortcutManager = getSystemService(ShortcutManager::class.java)
                val shortcut_simsweb = ShortcutInfo.Builder(this, "id1")
                        .setShortLabel("simsweb")
                        .setLongLabel("simsweb")
                        .setIcon(Icon.createWithResource(this, R.drawable.ic_link))
                        .setIntent(Intent(Intent.ACTION_VIEW, Uri.parse("http://simsweb.uitm.edu.my/SPORTAL_APP/SPORTAL_LOGIN/index.htm")))
                        .build()
                val shortcut_icress = ShortcutInfo.Builder(this, "id2")
                        .setShortLabel("iCress")
                        .setLongLabel("iCress")
                        .setIcon(Icon.createWithResource(this, R.drawable.ic_link))
                        .setIntent(Intent(Intent.ACTION_VIEW, Uri.parse("http://icress.uitm.edu.my/")))
                        .build()
                shortcutManager.dynamicShortcuts = Arrays.asList(shortcut_simsweb, shortcut_icress)
            }
        }
        val manager = supportFragmentManager
        manager.beginTransaction().replace(R.id.content, ActivityCourse()).commit()
        val navigation = findViewById<View>(R.id.navigation) as BottomNavigationView
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        // check for opening from widget
        val bundle = intent.extras
        if (bundle != null) {
            val extras = bundle.getString("Source")
            if (extras != null) {
                if (extras.contains("Widget")) {
                    navigation.selectedItemId = R.id.navigation_list
                }
            }
        }
        contextMain = this.baseContext
    }

    companion object {
        var contextMain: Context? = null
    }
}