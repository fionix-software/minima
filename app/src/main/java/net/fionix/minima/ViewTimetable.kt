package net.fionix.minima

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.TypedValue
import android.view.Gravity
import android.widget.RelativeLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.setPadding
import net.fionix.minima.model.EntityTimetable
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class ViewTimetable(val context: Context) {

    // hours count start from 8 AM and ends at 10 PM
    private val hoursCount = 14

    // timetable sizing info
    private val dayCellHeight = 120
    private val timeCellWidth = 100
    private val endCellWidth = 50
    private var timetableCellHeight = dayCellHeight
    private var timetableCellWidth = 0

    fun initTimetableView(tableLayout: TableLayout) {

        // get display info
        initSizingInfo()

        // generate table header (days)
        tableLayout.addView(generateTableHeader(), TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.MATCH_PARENT))

        // row count
        for (i in 0..hoursCount) {

            // create row
            val tableRow = TableRow(context)

            // create time indication cell
            val timeTextView = TextView(context)
            timeTextView.text = (i + 8).toString()
            timeTextView.gravity = Gravity.CENTER
            tableRow.addView(timeTextView)

            // add text view header for each column
            context.resources.getStringArray(R.array.timetable_weekdays).toList().forEach { _ ->

                // generate box for content
                val textView = TextView(context)
                textView.height = timetableCellHeight
                textView.width = timetableCellWidth
                timeTextView.gravity = Gravity.CENTER
                textView.background = ResourcesCompat.getDrawable(context.resources, R.drawable.view_timetable_border, null)
                tableRow.addView(textView)

            }

            // generate table header (content)
            tableLayout.addView(tableRow, TableLayout.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT))
        }
    }

    fun generateTimetableSticker(relativeLayout: RelativeLayout, importTimetableData: ArrayList<EntityTimetable>) {

        // sticker
        importTimetableData.forEach {

            // set sticker relative layout info
            val param: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(timetableCellWidth, calculateHeight(it.timetableTimeStart, it.timetableTimeEnd))
            param.addRule(RelativeLayout.ALIGN_PARENT_TOP)
            param.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
            param.setMargins(timeCellWidth + calculateDayOffset(it.timetableDay), dayCellHeight + calculateHeight(context.getString(R.string.uni_start_time), it.timetableTimeStart), 0, 0)

            // set textview info
            val tv = TextView(context)
            tv.layoutParams = param
            tv.gravity = Gravity.CENTER
            tv.text = context.getString(R.string.timetable_sticker_formatting, it.courseCode, it.timetableVenue)
            tv.setPadding(10)
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10f)
            tv.setTypeface(null, Typeface.BOLD)
            tv.setTextColor(Color.parseColor(context.getString(R.string.timetable_sticker_text_color)))
            tv.setBackgroundColor(Color.parseColor(context.getString(R.string.timetable_sticker_color)))

            // add into relative layout
            relativeLayout.addView(tv)
        }
    }

    private fun initSizingInfo() {
        val displayMetrics = context.resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        timetableCellWidth = (screenWidth - timeCellWidth - endCellWidth) / context.resources.getStringArray(R.array.timetable_header_title).toList().size
    }

    private fun generateTableHeader(): TableRow {

        // create row
        val tableRow = TableRow(context)

        // empty
        val emptyTextView = TextView(context)
        emptyTextView.height = dayCellHeight
        emptyTextView.width = timeCellWidth
        emptyTextView.gravity = Gravity.CENTER
        tableRow.addView(emptyTextView)

        // add text view header for each column
        val days: List<String> = context.resources.getStringArray(R.array.timetable_header_title).toList()
        days.forEach {

            val textView = TextView(context)
            textView.text = it
            textView.height = dayCellHeight
            textView.width = timeCellWidth
            textView.gravity = Gravity.CENTER
            tableRow.addView(textView)
        }

        // return table row
        return tableRow
    }

    private fun calculateDayOffset(day: String): Int {
        return ((context.resources.getStringArray(R.array.timetable_weekdays).toList().indexOf(day)) * timetableCellWidth)
    }

    private fun calculateHeight(timeStart: String, timeEnd: String): Int {

        // prepare format
        val formatter: DateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

        // parse start time
        val timetableStartTime: Date? = formatter.parse(timeStart)
        val timetableEndTime: Date? = formatter.parse(timeEnd)

        // calculate time offset
        var timeOffset = 0
        if (timetableStartTime != null && timetableEndTime != null) {
            timeOffset = TimeUnit.MILLISECONDS.toMinutes(timetableEndTime.time - timetableStartTime.time).toInt()
        }

        // return
        return (timetableCellHeight / 60) * timeOffset
    }
}