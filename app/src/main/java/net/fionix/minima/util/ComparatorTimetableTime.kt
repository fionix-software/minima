package net.fionix.minima.util

import net.fionix.minima.model.EntityTimetable
import java.text.SimpleDateFormat
import java.util.*

class ComparatorTimetableTime : Comparator<EntityTimetable> {
    override fun compare(p0: EntityTimetable, p1: EntityTimetable): Int {

        // date
        val formatter = SimpleDateFormat("h:mm a", Locale.getDefault())
        val d0: Date?
        val d1: Date?

        // current item
        try {
            d0 = formatter.parse(p0.timetableTimeStart)
        } catch (e: Exception) {
            // Please refer to UtilData.sortTimetable documentation
            return -1
        }

        // next item
        try {
            d1 = formatter.parse(p1.timetableTimeStart)
        } catch (e: Exception) {
            // Please refer to UtilData.sortTimetable documentation
            return 1
        }

        // compare
        return d0.compareTo(d1);
    }
}