package net.fionix.minima.util

import net.fionix.minima.model.EntityTimetable
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object UtilHtmlGenerator {

    //
    // fun generateTable(day: String, arrayListTimetable: ArrayList<ClassTimetable?>?): String? {
    //     val dayCode: Char
    //     dayCode = if (day == "Monday") {
    //         'A'
    //     } else if (day == "Tuesday") {
    //         'B'
    //     } else if (day == "Wednesday") {
    //         'C'
    //     } else if (day == "Thursday") {
    //         'D'
    //     } else if (day == "Friday") {
    //         'E'
    //     } else if (day == "Saturday") {
    //         'F'
    //     } else if (day == "Sunday") {
    //         'G'
    //     } else {
    //         'Z'
    //     }
    //     var html: String? = "<tr><td style=\"background: rgb(211,211,211);\">$day</td>"
    //     var lastEndCol = 0
    //     var colspan = 0
    //     for (i in arrayListTimetable!!.indices) {
    //         if (Character.toString(arrayListTimetable[i]!!.dayCode[0]) == Character.toString(dayCode)) {
    //
    //             // get start hour
    //             val charIndexHourStart = arrayListTimetable[i]!!.start.indexOf(":")
    //             var extractedHour = arrayListTimetable[i]!!.start.substring(0, charIndexHourStart)
    //             var intHourStart = Integer.valueOf(extractedHour)
    //             if (arrayListTimetable[i]!!.start.contains("pm")) {
    //                 if (intHourStart != 12) {
    //                     intHourStart += 12
    //                 }
    //             }
    //
    //             // get end hour
    //             val charIndexHourEnd = arrayListTimetable[i]!!.end.indexOf(":")
    //             extractedHour = arrayListTimetable[i]!!.end.substring(0, charIndexHourEnd)
    //             var intHourEnd = Integer.valueOf(extractedHour)
    //             if (arrayListTimetable[i]!!.end.contains("pm")) {
    //                 if (intHourEnd != 12) {
    //                     intHourEnd += 12
    //                 }
    //             }
    //
    //             // timetable
    //             var colStart = (intHourStart - 8) * 2 + 2
    //             if (!arrayListTimetable[i]!!.start.contains(":00 ")) {
    //                 colStart += 1
    //             }
    //             var colEnd = (intHourEnd - 8) * 2 + 1
    //             if (!arrayListTimetable[i]!!.end.contains(":00 ")) {
    //                 colEnd += 1
    //             }
    //
    //             // filler
    //             colspan = if (lastEndCol > 0) {
    //                 colStart - lastEndCol - 1
    //             } else {
    //                 colStart - 2
    //             }
    //             if (colspan > 1) {
    //                 html += "<td style=\"background: rgb(169,169,169);\" colspan=\"$colspan\"></td>"
    //             }
    //             if (colspan == 1) {
    //                 html += "<td style=\"background: rgb(169,169,169);\"></td>"
    //             }
    //
    //             // timetable
    //             colspan = colEnd - colStart + 1
    //             html += "<td colspan=\"" + colspan + "\">" + arrayListTimetable[i]!!.course + "<br>"
    //
    //             // add location and time
    //             html += if (arrayListTimetable[i]!!.location.isEmpty()) {
    //                 "-"
    //             } else {
    //                 arrayListTimetable[i]!!.location
    //             }
    //             html += "<br>" + arrayListTimetable[i]!!.start + " - " + arrayListTimetable[i]!!.end + "</td>"
    //             lastEndCol = colEnd
    //         }
    //     }
    //
    //     // fill timetable
    //     colspan = if (lastEndCol > 0) {
    //         31 - lastEndCol
    //     } else {
    //         31 - 1
    //     }
    //     html += "<td style=\"background: rgb(169,169,169);\" colspan=\"$colspan\"></td>"
    //     html += "</tr>"
    //     return html
    // }
    //
    // fun generateCourseList(arrayListCourse: ArrayList<ClassCourse?>?): String {
    //
    //     // create course list as unordered list
    //     var html = "<p><u>Course list</u></p><ul>"
    //
    //     // course listing
    //     for (i in arrayListCourse!!.indices) {
    //         html += "<li><strong>" + arrayListCourse[i]!!.group + ", " + arrayListCourse[i]!!.code + "</strong>: "
    //         html += if (arrayListCourse[i]!!.title.isEmpty()) {
    //             "-"
    //         } else {
    //             arrayListCourse[i]!!.title + "</li>"
    //         }
    //     }
    //
    //     // close listing
    //     html += "</ul>"
    //     return html
    // }
    //

    fun checkOverlapTimetableExist(dataSet: ArrayList<EntityTimetable>): Boolean {

        // iterate through by index
        val formatter: DateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        for (index in 0 until dataSet.size) {
            for (comparingIndex in 0 until dataSet.size) {

                // skip same index
                if (index == comparingIndex) {
                    continue
                }

                // only compare within same day
                if (dataSet[index].timetableDay != dataSet[comparingIndex].timetableDay) {
                    continue
                }

                // parse time
                val d0: Date?
                val d1Start: Date?
                val d1End: Date?
                try {
                    d0 = formatter.parse(dataSet[index].timetableTimeStart)
                    d1Start = formatter.parse(dataSet[comparingIndex].timetableTimeStart)
                    d1End = formatter.parse(dataSet[comparingIndex].timetableTimeEnd)
                } catch (e: Exception) {
                    continue
                }

                // check overlap and return
                if (d0.after(d1Start) && d0.before(d1End)) {
                    return true
                }
            }
        }

        // return
        return false
    }
}