package net.fionix.minima.util

import android.content.Context
import androidx.preference.PreferenceManager

class UtilTheme {

    enum class ThemeStatus(val id: Int, val text: String) {
        LIGHT(0, "Light theme"),
        DARK(1, "Dark theme"),
        SYSTEM_DEFAULT(2, "System default")
    }

    companion object {
        private const val THEME_STATUS = "net.fionix.minima.THEME_STATUS"

        fun getSharedPrefValue(context: Context?): ThemeStatus {
            val themeStatusSharedPref = PreferenceManager.getDefaultSharedPreferences(context).getInt(THEME_STATUS, ThemeStatus.SYSTEM_DEFAULT.id)
            return ThemeStatus.values()[themeStatusSharedPref]
        }

        fun setSharedPrefValue(context: Context?, themeStatus: ThemeStatus) {
            PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(THEME_STATUS, themeStatus.id).apply()
        }
    }

}