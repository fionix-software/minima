package net.fionix.minima

import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.fionix.minima.database.DatabaseMain
import net.fionix.minima.model.EntityTimetable
import net.fionix.minima.model.ModelFaculty
import net.fionix.minima.util.ScraperICress

class DialogAdd(context: Context) : Dialog(context) {

    init {
        setCanceledOnTouchOutside(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        // super
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_add)

        // progress bar
        val progressBar: ProgressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.GONE;

        // edit texts
        val courseCodeEditText: EditText = findViewById(R.id.editText1)
        val courseGroupEditText: EditText = findViewById(R.id.editText2)

        // add button
        val addButton: Button = findViewById(R.id.button1)
        addButton.setOnClickListener {

            // get input
            val courseCode: String = courseCodeEditText.text.toString().trim()
            val courseGroup: String = courseGroupEditText.text.toString().trim()
            if (courseCode.isEmpty() || courseGroup.isEmpty()) {
                return@setOnClickListener
            }

            // enable progress bar
            progressBar.visibility = View.VISIBLE;

            // start coroutine
            GlobalScope.launch(Dispatchers.IO) {

                // retrieve faculty
                val facultyList: ArrayList<ModelFaculty> = ScraperICress.retrieveFacultyList()
                if (facultyList.isEmpty()) {
                    // notify
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Failed to get faculty information", Toast.LENGTH_SHORT).show()
                        progressBar.visibility = View.GONE;
                    }
                    return@launch
                }

                // retrieve timetable
                val timetableList: ArrayList<EntityTimetable> = ScraperICress.retrieveTimetable(facultyList, courseCode, courseGroup)
                if (timetableList.isEmpty()) {

                    // notify
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Failed to get timetable information", Toast.LENGTH_SHORT).show()
                        progressBar.visibility = View.GONE;
                    }
                    return@launch
                }

                // save into database
                for (item in timetableList) {
                    DatabaseMain.getDatabase(context).timetableDao().insert(item)
                }

                // notify
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE;
                }
            }
        }
    }

}