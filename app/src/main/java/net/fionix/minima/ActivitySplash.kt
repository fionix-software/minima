package net.fionix.minima

import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import net.fionix.minima.util.UtilTheme

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

        // shortcuts
        val iCressShortcut = ShortcutInfo.Builder(applicationContext, "link_icress").setShortLabel(getString(R.string.shortcut_icress_short)).setLongLabel(getString(R.string.shortcut_icress_long)).setIcon(Icon.createWithResource(applicationContext, R.drawable.ic_external_link_square_alt_solid)).setIntent(Intent(Intent.ACTION_VIEW, Uri.parse("http://icress.uitm.edu.my/"))).build()
        val studentPortalShortcut = ShortcutInfo.Builder(applicationContext, "link_student_portal").setShortLabel(getString(R.string.shortcut_student_portal_short)).setLongLabel(getString(R.string.shortcut_student_portal_long)).setIcon(Icon.createWithResource(applicationContext, R.drawable.ic_external_link_square_alt_solid)).setIntent(Intent(Intent.ACTION_VIEW, Uri.parse("https://simsweb.uitm.edu.my/SPORTAL_APP/SPORTAL_LOGIN/index.htm"))).build()

        // add dynamic shortcuts
        val shortcutManager = getSystemService(ShortcutManager::class.java)
        shortcutManager!!.dynamicShortcuts = listOf(iCressShortcut, studentPortalShortcut)

        // set theme based on shared preference
        when (UtilTheme.getSharedPrefValue(this)) {
            // light
            UtilTheme.ThemeStatus.LIGHT -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            // dark
            UtilTheme.ThemeStatus.DARK -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            // system default
            else -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }
    }
}