package net.fionix.minima

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat

class ActivitySettings : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        // generate preference layout from resource
        addPreferencesFromResource(R.xml.activity_settings)

        // todo: add preference items

    }

}