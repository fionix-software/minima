package net.fionix.minima

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.fionix.minima.database.DatabaseMain
import net.fionix.minima.model.ModelCourse

class DialogEdit(context: Context, val data: ModelCourse) : Dialog(context) {

    init {
        setCanceledOnTouchOutside(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        // super
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_edit)

        // edit text
        val courseNameEditText: EditText = findViewById(R.id.editText1)
        courseNameEditText.setText(if (data.courseName.isEmpty()) context.getString(R.string.not_available) else data.courseName)

        // save button
        val saveButton: Button = findViewById(R.id.saveButton)
        saveButton.setOnClickListener {

            // check if string is empty
            val courseName = courseNameEditText.text.toString().trim()
            if (courseName.isEmpty() || courseName == data.courseName || courseName == context.getString(R.string.not_available)) {
                Toast.makeText(context, "Invalid course name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // update course name
            GlobalScope.launch(Dispatchers.IO) {

                // update course name
                DatabaseMain.getDatabase(context).timetableDao().updateName(data.courseCode, courseName, data.courseGroup, data.facultyCode, data.facultyName)

                // close dialog
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                    dismiss()
                }
            }
        }

        val deleteButton: Button = findViewById(R.id.deleteButton)
        deleteButton.setOnClickListener {

            // delete course
            GlobalScope.launch(Dispatchers.IO) {

                // delete
                DatabaseMain.getDatabase(context).timetableDao().deleteByCourse(data.courseCode, data.courseName, data.courseGroup, data.facultyCode, data.facultyName)

                // close dialog
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                    dismiss()
                }
            }
        }
    }
}