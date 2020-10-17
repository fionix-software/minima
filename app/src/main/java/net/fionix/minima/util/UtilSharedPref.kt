package net.fionix.minima.util

import android.content.Context
import androidx.preference.PreferenceManager

class UtilSharedPref {

    companion object {
        fun saveString(context: Context, variable: String, value: String) {
            PreferenceManager.getDefaultSharedPreferences(context).edit().putString(variable, value).apply()
        }

        fun loadString(context: Context, variable: String): String {
            return PreferenceManager.getDefaultSharedPreferences(context).getString(variable, "").toString()
        }
    }
}