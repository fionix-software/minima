package net.fionix.minima.test

import net.fionix.minima.model.EntityTimetable

class TestData {
    companion object {
        val databaseDataArray: ArrayList<EntityTimetable> = arrayListOf(
                EntityTimetable(0, "-", "AAA", "-", "-", "-", "-", "-", "Wednesday", "-"),
                EntityTimetable(0, "-", "AAA", "-", "-", "-", "-", "-", "Monday", "-"),
                EntityTimetable(0, "-", "AAA", "-", "-", "-", "-", "-", "Friday", "-"),
                EntityTimetable(0, "-", "BBB", "-", "-", "-", "-", "-", "Friday", "-"),
                EntityTimetable(0, "-", "BBB", "-", "-", "-", "-", "-", "Tuesday", "-"),
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
    }
}