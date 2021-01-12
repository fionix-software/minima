package net.fionix.minima

import android.app.Dialog
import android.content.Context
import android.os.Bundle
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
import net.fionix.minima.util.UtilData
import net.fionix.minima.util.UtilScraper

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
        progressBar.visibility = View.GONE

        // edit texts
        val courseCodeEditText: EditText = findViewById(R.id.editText1)
        val courseGroupEditText: EditText = findViewById(R.id.editText2)

        // add button
        val addButton: Button = findViewById(R.id.addButton)
        addButton.setOnClickListener {

            // get input
            val courseCode: String = courseCodeEditText.text.toString().trim()
            val courseGroup: String = courseGroupEditText.text.toString().trim()
            if (courseCode.isEmpty() || courseGroup.isEmpty()) {
                Toast.makeText(context, context.getString(R.string.invalid_course_info), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // enable progress bar
            addButton.isEnabled = false
            progressBar.visibility = View.VISIBLE

            // start coroutine
            GlobalScope.launch(Dispatchers.IO) {

                // retrieve faculty
                val facultyList: ArrayList<ModelFaculty> = UtilScraper.retrieveFacultyList()
                if (facultyList.isEmpty()) {
                    // notify
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, context.getString(R.string.failed_get_faculty_info), Toast.LENGTH_SHORT).show()
                        progressBar.visibility = View.GONE
                        addButton.isEnabled = true
                    }
                    return@launch
                }

                // retrieve timetable
                val timetableList: ArrayList<EntityTimetable> = UtilScraper.retrieveTimetable(facultyList, courseCode, courseGroup)
                val fixedTimetableList = UtilData.mergeClass(UtilData.sortTimetable(UtilData.fixTimetable(timetableList)))
                if (fixedTimetableList.isEmpty()) {

                    // notify
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, context.getString(R.string.failed_get_timetable_info), Toast.LENGTH_SHORT).show()
                        progressBar.visibility = View.GONE
                        addButton.isEnabled = true
                    }
                    return@launch
                }

                // save into database
                for (item in fixedTimetableList) {
                    DatabaseMain.getDatabase(context).timetableDao().insert(item)
                }

                // notify
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, context.getString(R.string.success), Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE
                    addButton.isEnabled = true
                }
            }
        }
    }
}