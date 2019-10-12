package net.fionix.minima;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Toast;

import java.util.ArrayList;

public class ActivitySettings extends PreferenceFragmentCompat {

    public static Context context;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {

        addPreferencesFromResource(R.xml.activity_settings);
        context = this.getContext();

        Preference preference_guide = getPreferenceManager().findPreference("preference_guide");
        preference_guide.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Guide");

                // Documentation string
                String text = getResources().getString(R.string.guide);

                builder.setMessage(text);
                builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                builder.show();

                return true;
            }
        });

        Preference preference_reset = getPreferenceManager().findPreference("preference_reset");
        preference_reset.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:

                                // resets timetable, course and faculty
                                ArrayList<ClassTimetable> arrayListTimetable = new ArrayList<>();
                                ClassMinima.saveTimetable(arrayListTimetable, context);

                                ArrayList<ClassCourse> arrayListCourse = new ArrayList<>();
                                ClassMinima.saveCourse(arrayListCourse, context);

                                ArrayList<ClassFaculty> arrayListFaculty = new ArrayList<>();
                                ClassMinima.saveFaculty(arrayListFaculty, context);

                                Toast.makeText(context, "Reset timetable success", Toast.LENGTH_SHORT).show();

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:

                                Toast.makeText(context, "Reset timetable cancelled", Toast.LENGTH_SHORT).show();

                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure to reset all timetable, course and faculty data?")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener)
                        .show();

                return true;
            }
        });

        Preference preference_student_portal = getPreferenceManager().findPreference("preference_student_portal");
        preference_student_portal.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://simsweb.uitm.edu.my/SPORTAL_APP/SPORTAL_LOGIN/index.htm"));
                startActivity(browserIntent);

                return true;
            }
        });

        Preference preference_icress = getPreferenceManager().findPreference("preference_icress");
        preference_icress.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://icress.uitm.edu.my/"));
                startActivity(browserIntent);

                return true;
            }
        });

        Preference preference_contribute = getPreferenceManager().findPreference("preference_conribute");
        preference_contribute.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/nazebzurati/minima"));
                startActivity(browserIntent);

                return true;
            }
        });

        Preference preference_email = getPreferenceManager().findPreference("preference_email");
        preference_email.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                String[] TO = {"nazeb04@gmail.com"};
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("message/rfc822");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback for Minima app");

                try {
                    startActivity(Intent.createChooser(emailIntent, "Send email via ..."));
                } catch (Exception exc) {
                    Toast.makeText(getActivity(), "No email client installed", Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });

        Preference preference_table = getPreferenceManager().findPreference("preference_table");
        preference_table.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                ArrayList<ClassTimetable> arrayListTimetable = ClassMinima.loadTimetable(getContext());
                ArrayList<ClassCourse> arrayListCourse = ClassMinima.loadCourse(getContext());

                // check for empty timetable first
                if (!arrayListTimetable.isEmpty() && arrayListTimetable.size() > 0) {
                    if (!arrayListCourse.isEmpty() && arrayListCourse.size() > 0) {
                        if (!ClassMinima.checkDuplicateCourse(arrayListCourse)) {
                            if (!ClassMinima.checkOverlapTimetableTime(arrayListTimetable)) {
                                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                    // open table activity
                                    Intent intentTable = new Intent(getContext(), ActivityTable.class);
                                    startActivity(intentTable);
                                } else {
                                    // request permission
                                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                                    Toast.makeText(getActivity(), "Unable to get write data to external storage permission. Please allow and restart the application.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), "There is time overlapping in your timetable. Please check your timetable.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "There is at least one of the same course. Please check your course list.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Your course is empty. Please add some course first.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Your timetable is empty. Please add some course first.", Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });

        Preference preference_version = getPreferenceManager().findPreference("preference_version");
        preference_version.setTitle("Minima v" + BuildConfig.VERSION_NAME);
        preference_version.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/nazebzurati"));
                startActivity(browserIntent);

                return true;
            }
        });
    }
}