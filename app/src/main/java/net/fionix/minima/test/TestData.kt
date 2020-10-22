package net.fionix.minima.test

import net.fionix.minima.model.EntityTimetable

class TestData {
    companion object {
        val databaseDataArray: ArrayList<EntityTimetable> = arrayListOf(
                EntityTimetable(0, "-", "AAA", "-", "-", "-", "-", "-", "Wednesday", "Venue 1"),
                EntityTimetable(0, "-", "AAA", "-", "-", "-", "-", "-", "Monday", "Venue 1"),
                EntityTimetable(0, "-", "AAA", "-", "-", "-", "-", "-", "Friday", "Venue 2"),
                EntityTimetable(0, "-", "BBB", "-", "-", "-", "-", "-", "Friday", "Venue 1"),
                EntityTimetable(0, "-", "BBB", "-", "-", "-", "-", "-", "Tuesday", "Venue 3"),
        )
        val fixNoonDataArray: ArrayList<EntityTimetable> = arrayListOf(
                EntityTimetable(0, "-", "-", "-", "-", "-", "12:00 am", "2:00 pm", "-", "-"),
                EntityTimetable(0, "-", "-", "-", "-", "-", "10:00 am", "12:00 am", "-", "-"),
        )
        val fix12hFormatDataArray: ArrayList<EntityTimetable> = arrayListOf(
                EntityTimetable(0, "-", "-", "-", "-", "-", "13:00 pm", "2:00 pm", "-", "-"),
                EntityTimetable(0, "-", "-", "-", "-", "-", "11:00 am", "13:00 pm", "-", "-"),
        )
        val fixMinute15DataArray: ArrayList<EntityTimetable> = arrayListOf(
                EntityTimetable(0, "-", "-", "-", "EE", "-", "1:5 pm", "2:00 pm", "-", "-"),
                EntityTimetable(0, "-", "-", "-", "EE", "-", "12:00 pm", "2:5 pm", "-", "-"),
        )
        val fixFacultyNameDataArray: ArrayList<EntityTimetable> = arrayListOf(
                EntityTimetable(0, "-", "-", "-", "MA", "(UiTM Kelantan [HEA/JW/05-2007)", "-", "-", "-", "-"),
                EntityTimetable(0, "-", "-", "-", "AR", "PERLIS", "-", "-", "-", "-"),
                EntityTimetable(0, "-", "-", "-", "AR", "Perlis", "-", "-", "-", "-"),
                EntityTimetable(0, "-", "-", "-", "KP", "Cawangan  N. Sembilan", "-", "-", "-", "-"),
                EntityTimetable(0, "-", "-", "-", "HP", "HEP SHAH ALAM", "-", "-", "-", "-"),
                EntityTimetable(0, "-", "-", "-", "MA", "UiTM Kelantan [HEA/JW/05-2007]", "-", "-", "-", "-"),
                EntityTimetable(0, "-", "-", "-", "PI", "PUSAT PEMIKIRAN DAN KEFAHAMAN ISLAM (CITU)", "-", "-", "-", "-"),
                EntityTimetable(0, "-", "-", "-", "SP", "Kampus Kedah", "-", "-", "-", "-"),
                EntityTimetable(0, "-", "-", "-", "SI", "KAMPUS PERAK", "-", "-", "-", "-"),
                EntityTimetable(0, "-", "-", "-", "PB", "Kampus Shah Alam - Akademi Pengajian Bahasa", "-", "-", "-", "-"),
        )
        val fixDayCaseDataArray: ArrayList<EntityTimetable> = arrayListOf(
                EntityTimetable(0, "-", "-", "-", "-", "-", "-", "-", "Thursday", "-"),
                EntityTimetable(0, "-", "-", "-", "-", "-", "-", "-", "ThurSday", "-"),
                EntityTimetable(0, "-", "-", "-", "-", "-", "-", "-", "THURSDAY", "-"),
                EntityTimetable(0, "-", "-", "-", "-", "-", "-", "-", "thursday", "-"),
        )
        val sortTimetableCaseDataArray: ArrayList<EntityTimetable> = arrayListOf(
                EntityTimetable(0, "-", "-", "-", "-", "-", "10:00 AM", "-", "Wednesday", "-"),
                EntityTimetable(0, "-", "-", "-", "-", "-", "12:00 PM", "-", "Monday", "-"),
                EntityTimetable(0, "-", "-", "-", "-", "-", "-", "-", "Friday", "-"),
                EntityTimetable(0, "-", "-", "-", "-", "-", "3:00 PM", "-", "Wednesday", "-"),
                EntityTimetable(0, "-", "-", "-", "-", "-", "10:00 AM", "-", "Friday", "-"),
                EntityTimetable(0, "-", "-", "-", "-", "-", "8:00 AM", "-", "-", "-"),
                EntityTimetable(0, "-", "-", "-", "-", "-", "-", "-", "-", "-"),
        )
        val mergeClassDataArray: ArrayList<EntityTimetable> = arrayListOf(
                // merge case
                EntityTimetable(0, "A", "-", "A", "A", "-", "10:00 AM", "11:00 AM", "Monday", "-"),
                EntityTimetable(0, "A", "-", "A", "A", "-", "11:00 AM", "12:00 PM", "Monday", "-"), // merged
                // single course
                EntityTimetable(0, "B", "-", "A", "A", "-", "3:00 PM", "5:00 PM", "Tuesday", "-"),
                // different course group
                EntityTimetable(0, "A", "-", "A", "A", "-", "3:00 PM", "4:00 PM", "Friday", "-"),
                EntityTimetable(0, "A", "-", "B", "A", "-", "4:00 PM", "5:00 PM", "Friday", "-"),
                // different course code
                EntityTimetable(0, "B", "-", "B", "A", "-", "3:00 PM", "4:00 PM", "Friday", "-"),
                EntityTimetable(0, "C", "-", "B", "A", "-", "4:00 PM", "5:00 PM", "Friday", "-"),
                // different faculty code
                EntityTimetable(0, "D", "-", "D", "A", "-", "3:00 PM", "4:00 PM", "Friday", "-"),
                EntityTimetable(0, "D", "-", "D", "D", "-", "4:00 PM", "5:00 PM", "Friday", "-"),
                // recursive
                EntityTimetable(0, "A", "-", "D", "A", "-", "10:00 AM", "11:00 AM", "Monday", "-"),
                EntityTimetable(0, "A", "-", "D", "A", "-", "11:00 AM", "12:00 PM", "Monday", "-"), // merged
                EntityTimetable(0, "A", "-", "D", "A", "-", "12:00 PM", "1:00 PM", "Monday", "-"), // merged
        )
    }
}