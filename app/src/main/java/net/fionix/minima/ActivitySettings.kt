package net.fionix.minima

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import net.fionix.minima.util.OnButtonClickConfirmClearDataAlertDialog
import net.fionix.minima.util.OnButtonClickDismissAlertDialog

class ActivitySettings : PreferenceFragmentCompat() {

    private val iCressLink: String = "http://icress.uitm.edu.my/"
    private val studentPortalLink: String = "https://simsweb.uitm.edu.my/SPORTAL_APP/SPORTAL_LOGIN/index.htm"
    private val githubRepoLink: String = "https://github.com/fionix-software/minima"
    private val fionixPageLink: String = "https://fionix.net/"

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        // generate preference layout from resource
        addPreferencesFromResource(R.xml.activity_settings)

        // clear data preference
        preferenceManager.findPreference<Preference>("preference_clear")?.setOnPreferenceClickListener {

            // confirmation dialog
            val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(preferenceManager.context)
            alertDialogBuilder.setTitle("Are you sure to clear data?")
            alertDialogBuilder.setMessage("This operation is irreversible.")
            alertDialogBuilder.setPositiveButton("Yes", OnButtonClickConfirmClearDataAlertDialog(preferenceManager.context))
            alertDialogBuilder.setNegativeButton("No", OnButtonClickDismissAlertDialog {
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
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("nazeb04@gmail.com"))
            intent.putExtra(Intent.EXTRA_SUBJECT, "[Minima] Hi! I've my feedback for you")
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_text))
            intent.type = "message/rfc822"
            startActivity(Intent.createChooser(intent, "Send feedback via: "));
            true
        }

        // open github repo with browser
        preferenceManager.findPreference<Preference>("preference_contribute")?.setOnPreferenceClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(githubRepoLink)
            startActivity(intent)
            true
        }

        // open fionix page with browser
        preferenceManager.findPreference<Preference>("preference_version")?.title = getString(R.string.app_version).replace("%v", "v" + BuildConfig.VERSION_NAME)
        preferenceManager.findPreference<Preference>("preference_version")?.setOnPreferenceClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(fionixPageLink)
            startActivity(intent)
            true
        }
    }

}