package net.fionix.minima.util

import android.content.Context
import android.content.DialogInterface
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.fionix.minima.R
import net.fionix.minima.database.DatabaseMain
import net.fionix.minima.model.ModelCourse

class OnButtonClickConfirmDeleteCourseAlertDialog(private val context: Context, val data: ModelCourse, val functionCallback: () -> Unit) : DialogInterface.OnClickListener {

    override fun onClick(p0: DialogInterface?, p1: Int) {

        // delete course
        GlobalScope.launch(Dispatchers.IO) {

            // delete
            DatabaseMain.getDatabase(context).timetableDao().deleteByCourse(data.courseCode, data.courseName, data.courseGroup, data.facultyCode, data.facultyName)

            // close dialog
            withContext(Dispatchers.Main) {
                Toast.makeText(context, context.getString(R.string.success), Toast.LENGTH_SHORT).show()
                functionCallback()
                p0?.dismiss()
            }
        }
    }
}