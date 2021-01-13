package net.fionix.minima.util

import android.database.Cursor
import net.fionix.minima.model.EntityTimetable
import net.fionix.minima.model.ModelCourse
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

object UtilData {

    // this is a recursive function
    fun fixTimetable(dataSet: ArrayList<EntityTimetable>): ArrayList<EntityTimetable> {

        // make a copy from existing dataset and fix by iteration
        val tempTimetableList: MutableList<EntityTimetable> = dataSet.toMutableList()
        for (tempTimetable in tempTimetableList) {

            // lower case for easier fix
            tempTimetable.timetableTimeStart = tempTimetable.timetableTimeStart.toLowerCase(Locale.getDefault())
            tempTimetable.timetableTimeEnd = tempTimetable.timetableTimeEnd.toLowerCase(Locale.getDefault())

            // fix 12 noon as am
            if (tempTimetable.timetableTimeStart == "12:00 am") {
                tempTimetable.timetableTimeStart = "12:00 pm"
            }
            if (tempTimetable.timetableTimeEnd == "12:00 am") {
                tempTimetable.timetableTimeEnd = "12:00 pm"
            }

            // fix 24 hour format for 1 pm
            if (tempTimetable.timetableTimeStart == "13:00 pm") {
                tempTimetable.timetableTimeStart = "1:00 pm"
            }
            if (tempTimetable.timetableTimeEnd == "13:00 pm") {
                tempTimetable.timetableTimeEnd = "1:00 pm"
            }

            // fix XX:15 time represent as XX:5
            if (tempTimetable.timetableTimeStart.contains(":5 ")) {
                tempTimetable.timetableTimeStart = tempTimetable.timetableTimeStart.replace(":5 ", ":15 ")
            }
            if (tempTimetable.timetableTimeEnd.contains(":5 ")) {
                tempTimetable.timetableTimeEnd = tempTimetable.timetableTimeEnd.replace(":5 ", ":15 ")
            }

            // fix MA campus
            if (tempTimetable.facultyName[0] == '(') {
                tempTimetable.facultyName = tempTimetable.facultyName.substring(1, tempTimetable.facultyName.length - 1) + ']'
            }

            // fix AR campus
            if (tempTimetable.facultyName.toLowerCase(Locale.getDefault()) == "perlis") {
                tempTimetable.facultyName = "kampus " + tempTimetable.facultyName
            }

            // fix day case
            tempTimetable.timetableDay = tempTimetable.timetableDay.toLowerCase(Locale.getDefault()).capitalize(Locale.getDefault())

            // fix faculty name case
            tempTimetable.facultyName = tempTimetable.facultyName.toLowerCase(Locale.getDefault()).replace("  ", " ")
            val tempFacultyNameList = ArrayList(tempTimetable.facultyName.split(' '))
            for (index in tempFacultyNameList.indices) {

                // trim string
                tempFacultyNameList[index].trim()

                // fix partial upper case
                if (tempFacultyNameList[index].contains("uitm")) {
                    tempFacultyNameList[index] = tempFacultyNameList[index].replace("uitm", "UiTM")
                } else if (tempFacultyNameList[index] == "n." || tempFacultyNameList[index] == "hep" || (tempFacultyNameList[index][0] == '[' && tempFacultyNameList[index][tempFacultyNameList[index].length - 1] == ']') || (tempFacultyNameList[index][0] == '(' && tempFacultyNameList[index][tempFacultyNameList[index].length - 1] == ')')) {
                    tempFacultyNameList[index] = tempFacultyNameList[index].toUpperCase(Locale.getDefault())
                } else {
                    tempFacultyNameList[index] = tempFacultyNameList[index].capitalize(Locale.getDefault())
                }
            }
            tempTimetable.facultyName = tempFacultyNameList.joinToString(" ")
        }

        // return
        return ArrayList(tempTimetableList)
    }

    fun cursorToCourseList(cursor: Cursor): ArrayList<ModelCourse> {
        val arrayList: ArrayList<ModelCourse> = arrayListOf()
        cursor.use { c ->
            while (c.moveToNext()) {
                // column 0: course code
                // column 1: course name
                // column 2: course group
                // column 3: faculty code
                // column 4: faculty name
                arrayList.add(ModelCourse(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4)))
            }
        }
        return arrayList
    }

    fun checkValidTimetableTime(time: String): Boolean {
        val formatter = SimpleDateFormat("h:mm a", Locale.getDefault())
        try {
            formatter.parse(time)
        } catch (e: Exception) {
            return false
        }
        return true
    }

    // Getting day of the week using SimpleDateFormat depends on the first week in current year.
    // e.g. In 2020, Wed (07 Jan) while first day of the week start with Thursday (02 Jan)
    // * day of the week sequence use index 1 to 7 instead of 0 to 6 but start the date with index 0
    // This is not what user should expect. Thus, week by int method is used.
    fun getDayOfTheWeek(day: String): Int {
        when (day.toLowerCase(Locale.getDefault()).trim()) {
            "monday" -> {
                return 0
            }
            "tuesday" -> {
                return 1
            }
            "wednesday" -> {
                return 2
            }
            "thursday" -> {
                return 3
            }
            "friday" -> {
                return 4
            }
            "saturday" -> {
                return 5
            }
            "sunday" -> {
                return 6
            }
            else -> return 7
        }
    }

    // Invalid time will be pushed to front (lower index) and invalid day will be
    // pushed to the back (higher index).
    // Class day and invalid item should be in front to emphasize the importance
    // - Class day is required by default
    // - Class start time is no required but if invalid, should be highlighted to
    //   to lecturer
    fun sortTimetable(dataSet: ArrayList<EntityTimetable>): ArrayList<EntityTimetable> {

        // check for null and empty list
        if (dataSet.isEmpty()) {
            return arrayListOf()
        }

        // group by day
        val sortedList = arrayListOf<EntityTimetable>()
        dataSet.sortedWith(compareBy {
            getDayOfTheWeek(it.timetableDay)
        }).groupBy { it.timetableDay }.values.map {
            it.sortedWith(ComparatorTimetableTime()).toList()
        }.forEach {
            sortedList.addAll(it)
        }

        // return
        return sortedList
    }

    // There are cases where a same class for 2 hours split into 2 separate class.
    // There are no issue with this case but it only increase list item and it is a
    // good effort if these classes can be merged to reduce the list item and improve
    // user experience.
    fun mergeClass(dataSet: ArrayList<EntityTimetable>): ArrayList<EntityTimetable> {

        // merge class
        var mergedNext = false
        var itemChanged = false
        val mergedTimetableItem = arrayListOf<EntityTimetable>()
        for (index in 0 until dataSet.size) {

            // skip if previously has been merged
            if (mergedNext) {
                mergedNext = false
                continue
            }

            // prevent out of bound
            if (index == (dataSet.size - 1)) {
                mergedTimetableItem.add(dataSet[index])
                break
            }

            // merger
            if (dataSet[index].courseCode == dataSet[index + 1].courseCode && dataSet[index].courseGroup == dataSet[index + 1].courseGroup && dataSet[index].facultyCode == dataSet[index + 1].facultyCode && dataSet[index].timetableDay == dataSet[index + 1].timetableDay && dataSet[index].timetableTimeEnd == dataSet[index + 1].timetableTimeStart) {

                // fix the starting class to have end time from the next class (as its merged)
                val mergedData = dataSet[index]
                mergedData.timetableTimeEnd = dataSet[index + 1].timetableTimeEnd
                mergedTimetableItem.add(mergedData)

                // flag
                mergedNext = true
                itemChanged = true
            } else {
                mergedTimetableItem.add(dataSet[index])
            }
        }

        // recursive call
        if (itemChanged) {
            return mergeClass(mergedTimetableItem)
        }
        return mergedTimetableItem
    }

    fun checkOverlapTimetableExist(dataSet: java.util.ArrayList<EntityTimetable>): Boolean {

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