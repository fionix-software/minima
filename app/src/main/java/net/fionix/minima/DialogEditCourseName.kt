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

class DialogEditCourseName(context: Context, val data: ModelCourse) : Dialog(context) {

    init {
        setCanceledOnTouchOutside(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        // super
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_edit_course_name)

        // edit text
        val courseNameEditText: EditText = findViewById(R.id.editText1)
        if (data.courseName.isNotEmpty()) {
            courseNameEditText.setText(data.courseName)
        }

        // save button
        val saveButton: Button = findViewById(R.id.saveButton)
        saveButton.setOnClickListener {

            // check if string is empty
            val courseName = courseNameEditText.text.toString().trim()
            if (courseName.isEmpty() || courseName == data.courseName) {
                Toast.makeText(context, context.getString(R.string.invalid_course_name), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // update course name
            GlobalScope.launch(Dispatchers.IO) {

                // update course name
                DatabaseMain.getDatabase(context).timetableDao().updateCourseName(courseName, data.courseCode, data.courseName, data.facultyCode)

                // close dialog
                withContext(Dispatchers.Main) {

                    // display toast
                    Toast.makeText(context, context.getString(R.string.success), Toast.LENGTH_SHORT).show()
                    dismiss()
                }
            }
        }
    }
}