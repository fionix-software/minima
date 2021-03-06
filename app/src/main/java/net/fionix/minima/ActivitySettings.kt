package net.fionix.minima

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import net.fionix.minima.util.OnButtonClickConfirmClearDataAlertDialog
import net.fionix.minima.util.OnButtonClickDismissAlertDialog
import net.fionix.minima.util.UtilTheme

class ActivitySettings : PreferenceFragmentCompat() {

    private val iCressLink: String = "http://icress.uitm.edu.my/"
    private val studentPortalLink: String = "https://simsweb.uitm.edu.my/SPORTAL_APP/SPORTAL_LOGIN/index.htm"
    private val githubRepoLink: String = "https://github.com/fionix-software/minima"
    private val kofiPageLink: String = "https://ko-fi.com/fionix"
    private val fionixPageLink: String = "https://fionix.net/"
    private val developerEmail = arrayOf("nazeb04@gmail.com")

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        // generate preference layout from resource
        addPreferencesFromResource(R.xml.activity_settings)

        // theme
        val themeStatus = UtilTheme.getSharedPrefValue(context)
        preferenceManager.findPreference<Preference>("preference_theme")?.summary = themeStatus.text
        preferenceManager.findPreference<Preference>("preference_theme")?.setOnPreferenceClickListener {

            // get array from theme status enum
            val themeOption = arrayOfNulls<String>(UtilTheme.ThemeStatus.values().size)
            for (i in UtilTheme.ThemeStatus.values().indices) {
                themeOption[i] = UtilTheme.ThemeStatus.values()[i].text
            }

            // alert dialog builder
            val builder = AlertDialog.Builder(context)
            builder.setSingleChoiceItems(themeOption, themeStatus.id) { dialog, selectedId ->
                when (selectedId) {
                    // light
                    UtilTheme.ThemeStatus.LIGHT.id -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }
                    // dark
                    UtilTheme.ThemeStatus.DARK.id -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    }
                    // system default
                    else -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    }
                }
                UtilTheme.setSharedPrefValue(context, UtilTheme.ThemeStatus.values()[selectedId])
                it.summary = UtilTheme.getSharedPrefValue(context).text
                dialog.dismiss()
            }

            // show
            builder.create().show()
            true
        }

        // clear data preference
        preferenceManager.findPreference<Preference>("preference_clear")?.setOnPreferenceClickListener {

            // confirmation dialog
            val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(preferenceManager.context)
            alertDialogBuilder.setTitle(getString(R.string.dialog_clear_data_title))
            alertDialogBuilder.setMessage(getString(R.string.dialog_clear_data_message))
            alertDialogBuilder.setPositiveButton(getString(R.string.dialog_button_yes), OnButtonClickConfirmClearDataAlertDialog(preferenceManager.context))
            alertDialogBuilder.setNegativeButton(getString(R.string.dialog_button_no), OnButtonClickDismissAlertDialog {
                // noop
            })
            alertDialogBuilder.show()
            true
        }

        // open icress with browser
        preferenceManager.findPreference<Preference>("preference_icress")?.setOnPreferenceClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(iCressLink)
            startActivity(intent)
            true
        }

        // open student portal with browser
        preferenceManager.findPreference<Preference>("preference_student_portal")?.setOnPreferenceClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(studentPortalLink)
            startActivity(intent)
            true
        }

        // open email and use email template
        preferenceManager.findPreference<Preference>("preference_email")?.setOnPreferenceClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_EMAIL, developerEmail)
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject))
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_text))
            intent.type = "message/rfc822"
            startActivity(Intent.createChooser(intent, getString(R.string.email_intent_title)))
            true
        }

        // open github repo with browser
        preferenceManager.findPreference<Preference>("preference_contribute")?.setOnPreferenceClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(githubRepoLink)
            startActivity(intent)
            true
        }

        // open ko-fi page with browser
        preferenceManager.findPreference<Preference>("preference_donate")?.setOnPreferenceClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(kofiPageLink)
            startActivity(intent)
            true
        }

        // open fionix page with browser
        preferenceManager.findPreference<Preference>("preference_version")?.title = getString(R.string.app_version).replace("\$version", BuildConfig.VERSION_NAME)
        preferenceManager.findPreference<Preference>("preference_version")?.setOnPreferenceClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(fionixPageLink)
            startActivity(intent)
            true
        }
    }
}