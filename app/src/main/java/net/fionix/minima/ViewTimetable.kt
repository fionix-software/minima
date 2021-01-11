package net.fionix.minima

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.Typeface
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.Display
import android.view.Gravity
import android.view.WindowManager
import android.widget.RelativeLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.marginBottom
import androidx.core.view.setPadding
import net.fionix.minima.model.EntityTimetable
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


class ViewTimetable(val context: Context) {

    // hours count start from 8 AM
    private val uniStartTime = "08:00 am"
    private val hoursCount = 14

    private var TT_CELL_HEIGHT = 120
    private var TT_CELL_WIDTH = 0
    private val DAY_CELL_HEIGHT = 120
    private val TIME_CELL_WIDTH = 100
    private val END_CELL_WIDTH = 50

    private var timetableData = arrayListOf<EntityTimetable>()

    @JvmName("importTimetableData")
    fun setTimetableData(importTimetableData: ArrayList<EntityTimetable>, relativeLayout: RelativeLayout) {
        this.timetableData = importTimetableData

        // sticker
        timetableData.forEach {

            // set sticker relative layout info
            val param: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(TT_CELL_WIDTH, calculateTimeLengthOffset(it.timetableTimeStart, it.timetableTimeEnd))
            param.addRule(RelativeLayout.ALIGN_PARENT_TOP)
            param.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
            param.setMargins(TIME_CELL_WIDTH + calculateDayOffset(it.timetableDay), DAY_CELL_HEIGHT + calculateTimeLengthOffset(uniStartTime, it.timetableTimeStart), 0, 0)

            // set text view info
            val tv = TextView(context)
            tv.layoutParams = param
            tv.setPadding(10)
            tv.text = it.courseCode + "\n" + it.timetableVenue
            tv.setTextColor(Color.parseColor("#ffffff"))
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10f)
            tv.setTypeface(null, Typeface.BOLD)
            tv.gravity = Gravity.CENTER
            tv.setBackgroundColor((Color.parseColor("#000000")))

            // add into relative layout
            relativeLayout.addView(tv)
        }
    }

    @SuppressLint("SetTextI18n")
    fun generateTimetableView(tableLayout: TableLayout) {

        // get display info
        getDisplayInfo()

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
            val days: List<String> = context.resources.getStringArray(R.array.timetable_header_title).toList()
            days.forEach { _ ->
                val textView = TextView(context)
                textView.background = ResourcesCompat.getDrawable(context.resources, R.drawable.view_timetable_border, null)
                textView.height = DAY_CELL_HEIGHT
                textView.width = TT_CELL_WIDTH
                timeTextView.gravity = Gravity.CENTER
                tableRow.addView(textView)
            }

            // generate table header (content)
            tableLayout.addView(tableRow, TableLayout.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT))
        }
    }

    private fun generateTableHeader(): TableRow {

        // create row
        val tableRow = TableRow(context)

        // empty
        val emptyTextView = TextView(context)
        emptyTextView.height = DAY_CELL_HEIGHT
        emptyTextView.width = TIME_CELL_WIDTH
        emptyTextView.gravity = Gravity.CENTER
        tableRow.addView(emptyTextView)

        // add text view header for each column
        val days: List<String> = context.resources.getStringArray(R.array.timetable_header_title).toList()
        days.forEach {

            val textView = TextView(context)
            textView.text = it
            textView.height = DAY_CELL_HEIGHT
            textView.width = TIME_CELL_WIDTH
            textView.gravity = Gravity.CENTER
            tableRow.addView(textView)
        }

        // return table row
        return tableRow
    }

    private fun getDisplayInfo() {
        val displayMetrics = context.resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        TT_CELL_WIDTH = (screenWidth - TIME_CELL_WIDTH - END_CELL_WIDTH) / context.resources.getStringArray(R.array.timetable_header_title).toList().size
    }

    private fun calculateHeight(durationInMin: Int): Int {
        return (TT_CELL_HEIGHT / 60) * durationInMin
    }

    private fun calculateDayOffset(day: String): Int {
        return ((context.resources.getStringArray(R.array.timetable_weekdays).toList().indexOf(day)) * TT_CELL_WIDTH)
    }

    private fun calculateTimeLengthOffset(timeStart: String, timeEnd: String): Int {

        // prepare format
        val formatter: DateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

        // parse start time
        val timetableStartTime: Date? = formatter.parse(timeStart)
        val timetableEndTime: Date? = formatter.parse(timeEnd)

        // calculate time offset
        var timeOffset: Int = 0
        if (timetableStartTime != null && timetableEndTime != null) {
            timeOffset = TimeUnit.MILLISECONDS.toMinutes(timetableEndTime.time - timetableStartTime.time).toInt()
            Log.d("Offset", timeStart + "-" + timeEnd + "=" + timeOffset.toString() + "min -> " + calculateHeight(timeOffset).toString())
        }

        // return
        return calculateHeight(timeOffset)
    }
}