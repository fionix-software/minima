package net.fionix.minima.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService.RemoteViewsFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.fionix.minima.R
import net.fionix.minima.database.DatabaseMain
import net.fionix.minima.model.EntityTimetable
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class WidgetFactory(var context: Context, intent: Intent) : RemoteViewsFactory {

    private val appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID) - WidgetReceiver.randomNumber
    private lateinit var timetableList: ArrayList<EntityTimetable>

    override fun getViewAt(index: Int): RemoteViews {

        // view
        val removeViews = RemoteViews(context.packageName, R.layout.list_three_line_linear)

        // title
        var courseCode: String = timetableList[index].courseCode
        if (timetableList[index].courseName.isNotEmpty()) {
            courseCode = courseCode + " - " + timetableList[index].courseName
        }
        removeViews.setTextViewText(R.id.title_text, courseCode)

        // secondary
        val secondaryString: String = timetableList[index].timetableDay + ", " + timetableList[index].timetableTimeStart + " - " + timetableList[index].timetableTimeEnd
        removeViews.setTextViewText(R.id.secondary_text, secondaryString)

        // tertiary
        removeViews.setTextViewText(R.id.tertiary_text, timetableList[index].timetableVenue)

        // return
        return removeViews
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getCount(): Int {
        if (!this::timetableList.isInitialized) {
            return 0
        }
        return timetableList.size
    }

    override fun getItemId(index: Int): Long {
        return 0
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun onCreate() {
        GlobalScope.launch(Dispatchers.IO) {
            timetableList = ArrayList(DatabaseMain.getDatabase(context).timetableDao().getListByDay(SimpleDateFormat("EEEE", Locale.ENGLISH).format(Calendar.getInstance().time)))
        }
    }

    override fun onDataSetChanged() {}

    override fun onDestroy() {}

}