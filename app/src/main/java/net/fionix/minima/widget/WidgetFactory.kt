package net.fionix.minima.widget

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService.RemoteViewsFactory
import net.fionix.minima.R
import net.fionix.minima.model.ClassMinima
import net.fionix.minima.model.ClassTimetable
import java.util.*

class WidgetFactory(var context: Context, intent: Intent) : RemoteViewsFactory {
    var resourceId: Int
    var arrayListTimetable: ArrayList<ClassTimetable?>?
    override fun onCreate() {}
    override fun onDataSetChanged() {}
    override fun onDestroy() {}
    override fun getCount(): Int {
        return if (arrayListTimetable != null) {
            arrayListTimetable!!.size
        } else 0
    }

    override fun getViewAt(i: Int): RemoteViews {
        val row = RemoteViews(context.packageName, R.layout.listview_widget)
        var courseName = ""
        val arrayListCourseCode = ClassMinima.loadCourse(context)
        for (j in arrayListCourseCode!!.indices) {
            if (arrayListCourseCode[j]!!.code!!.contains(arrayListTimetable!![i]!!.course!!)) {
                courseName = if (arrayListCourseCode[j]!!.title.isEmpty()) "You can edit course name at the Course tab" else arrayListCourseCode[j]!!.title
                break
            }
        }
        row.setTextViewText(R.id.textViewCourseName, courseName)
        row.setTextViewText(R.id.textViewCourseCode, arrayListTimetable!![i]!!.course)
        row.setTextViewText(R.id.textViewGroup, arrayListTimetable!![i]!!.group)
        row.setTextViewText(R.id.textViewTime, arrayListTimetable!![i]!!.start + " - " + arrayListTimetable!![i]!!.end)
        row.setTextViewText(R.id.textViewLocation, arrayListTimetable!![i]!!.location.toUpperCase())
        return row
    }

    override fun getLoadingView(): RemoteViews {
        return null
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(i: Int): Long {
        return 0
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    init {
        resourceId = Integer.valueOf(intent.data.schemeSpecificPart) - WidgetProvider.randomNumber
        arrayListTimetable = ClassMinima.filterForToday(ClassMinima.loadTimetable(context))
    }
}