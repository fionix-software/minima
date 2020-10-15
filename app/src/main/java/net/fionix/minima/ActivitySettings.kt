package net.fionix.minima

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import net.fionix.minima.model.ClassCourse
import net.fionix.minima.model.ClassFaculty
import net.fionix.minima.model.ClassMinima
import net.fionix.minima.model.ClassTimetable
import java.util.*

class ActivitySettings : PreferenceFragmentCompat() {
    override fun onCreatePreferences(bundle: Bundle, s: String) {
        addPreferencesFromResource(R.xml.activity_settings)
        Companion.context = context
        val preference_guide = preferenceManager.findPreference("preference_guide")
        preference_guide.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val builder = AlertDialog.Builder(context!!)
            builder.setTitle("Guide")

            // Documentation string
            val text = resources.getString(R.string.guide)
            builder.setMessage(text)
            builder.setPositiveButton("Done") { dialog, id -> dialog.cancel() }
            builder.show()
            true
        }
        val preference_reset = preferenceManager.findPreference("preference_reset")
        preference_reset.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val dialogClickListener = DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {

                        // resets timetable, course and faculty
                        val arrayListTimetable = ArrayList<ClassTimetable?>()
                        ClassMinima.saveTimetable(arrayListTimetable, Companion.context)
                        val arrayListCourse = ArrayList<ClassCourse?>()
                        ClassMinima.saveCourse(arrayListCourse, Companion.context)
                        val arrayListFaculty = ArrayList<ClassFaculty>()
                        ClassMinima.saveFaculty(arrayListFaculty, Companion.context)
                        Toast.makeText(Companion.context, "Reset timetable success", Toast.LENGTH_SHORT).show()
                    }
                    DialogInterface.BUTTON_NEGATIVE -> Toast.makeText(Companion.context, "Reset timetable cancelled", Toast.LENGTH_SHORT).show()
                }
            }
            val builder = AlertDialog.Builder(Companion.context!!)
            builder.setMessage("Are you sure to reset all timetable, course and faculty data?")
                    .setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener)
                    .show()
            true
        }
        val preference_student_portal = preferenceManager.findPreference("preference_student_portal")
        preference_student_portal.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://simsweb.uitm.edu.my/SPORTAL_APP/SPORTAL_LOGIN/index.htm"))
            startActivity(browserIntent)
            true
        }
        val preference_icress = preferenceManager.findPreference("preference_icress")
        preference_icress.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://icress.uitm.edu.my/"))
            startActivity(browserIntent)
            true
        }
        val preference_contribute = preferenceManager.findPreference("preference_conribute")
        preference_contribute.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/nazebzurati/minima"))
            startActivity(browserIntent)
            true
        }
        val preference_email = preferenceManager.findPreference("preference_email")
        preference_email.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val TO = arrayOf("nazeb04@gmail.com")
            val emailIntent = Intent(Intent.ACTION_SEND)
            emailIntent.data = Uri.parse("mailto:")
            emailIntent.type = "message/rfc822"
            emailIntent.putExtra(Intent.EXTRA_EMAIL, TO)
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback for Minima app")
            try {
                startActivity(Intent.createChooser(emailIntent, "Send email via ..."))
            } catch (exc: Exception) {
                Toast.makeText(activity, "No email client installed", Toast.LENGTH_SHORT).show()
            }
            true
        }
        val preference_table = preferenceManager.findPreference("preference_table")
        preference_table.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val arrayListTimetable = ClassMinima.loadTimetable(context)
            val arrayListCourse = ClassMinima.loadCourse(context)

            // check for empty timetable first
            if (!arrayListTimetable!!.isEmpty() && arrayListTimetable.size > 0) {
                if (!arrayListCourse!!.isEmpty() && arrayListCourse.size > 0) {
                    if (!ClassMinima.checkDuplicateCourse(arrayListCourse)) {
                        if (!ClassMinima.checkOverlapTimetableTime(arrayListTimetable)) {
                            if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                // open table activity
                                val intentTable = Intent(context, ActivityTable::class.java)
                                startActivity(intentTable)
                            } else {
                                // request permission
                                ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 2)
                                Toast.makeText(activity, "Unable to get write data to external storage permission. Please allow and restart the application.", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(activity, "There is time overlapping in your timetable. Please check your timetable.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(activity, "There is at least one of the same course. Please check your course list.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(activity, "Your course is empty. Please add some course first.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(activity, "Your timetable is empty. Please add some course first.", Toast.LENGTH_SHORT).show()
            }
            true
        }
        val preference_version = preferenceManager.findPreference("preference_version")
        preference_version.title = "Minima v" + BuildConfig.VERSION_NAME
        preference_version.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/nazebzurati"))
            startActivity(browserIntent)
            true
        }
    }

    companion object {
        var context: Context? = null
    }
}