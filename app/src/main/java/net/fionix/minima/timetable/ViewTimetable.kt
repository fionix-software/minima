package net.fionix.minima.timetable

import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.marginBottom
import net.fionix.minima.R


class ViewTimetable(val context: Context) {

    private val rowCount = 14

    fun generateTimetableView(tableLayout: TableLayout) {

        // generate table header (days)
        tableLayout.addView(generateTableHeader(), TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.MATCH_PARENT))

        // row count
        for (i in 0..rowCount) {

            // create row
            val tableRow = TableRow(context)

            val timeTextView = TextView(context)
            timeTextView.text = (i + 8).toString()
            timeTextView.height = calculateRowHeight()
            timeTextView.gravity = Gravity.CENTER_HORIZONTAL
            tableRow.addView(timeTextView)

            // add text view header for each column
            val days: List<String> = context.resources.getStringArray(R.array.timetable_header_title).toList()
            days.drop(1).forEach {
                val textView = TextView(context)
                textView.background = ResourcesCompat.getDrawable(context.resources, R.drawable.view_timetable_border, null)
                textView.height = calculateRowHeight()
                tableRow.addView(textView)
            }

            tableLayout.addView(tableRow, TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT))
        }
    }

    private fun generateTableHeader(): TableRow {

        // create row
        val tableRow = TableRow(context)

        // add text view header for each column
        val days: List<String> = context.resources.getStringArray(R.array.timetable_header_title).toList()
        days.forEach {
            val textView = TextView(context)
            textView.text = it
            textView.height = 100
            textView.gravity = Gravity.CENTER
            tableRow.addView(textView)
        }

        // return table row
        return tableRow
    }

    private fun calculateRowHeight(): Int {
        return (70 / rowCount) * 20
    }
}