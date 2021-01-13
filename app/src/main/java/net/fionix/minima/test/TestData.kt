package net.fionix.minima.test

import net.fionix.minima.model.EntityTimetable

class TestData {
    companion object {
        val databaseDataArray: ArrayList<EntityTimetable> = arrayListOf(
                EntityTimetable(0, "ECE648", "Special topics", "EE241C8A", "EE", "Fakulti Kejuruteraan Elektrikal", "9:00 AM", "11:00 AM", "Monday", "ODL1"),
                EntityTimetable(0, "ECE643", "Network security", "EE241C8A", "EE", "Fakulti Kejuruteraan Elektrikal", "8:00 AM", "10:00 AM", "Tuesday", "ODL1"),
                EntityTimetable(0, "ECE648", "Special topics", "EE241C8A", "EE", "Fakulti Kejuruteraan Elektrikal", "2:00 PM", "4:00 PM", "Tuesday", "ODL1"),
                EntityTimetable(0, "ELE672", "Industrial Topic", "EE241C8A", "EE", "Fakulti Kejuruteraan Elektrikal", "9:00 AM", "11:00 AM", "Thursday", "ODL1"),
                EntityTimetable(0, "ECE643", "Network security", "EE241C8A", "EE", "Fakulti Kejuruteraan Elektrikal", "2:00 PM", "4:00 PM", "Thursday", "ODL1"),
                EntityTimetable(0, "ELE607", "EE Project 2", "EE241C8A", "EE", "Fakulti Kejuruteraan Elektrikal", "3:00 PM", "4:00 PM", "Friday", "ODL1"),
        )
        val databaseDuplicateDataArray: ArrayList<EntityTimetable> = arrayListOf(
                EntityTimetable(0, "ECE648", "Special topics", "EE241C8A", "EE", "Fakulti Kejuruteraan Elektrikal", "9:00 AM", "11:00 AM", "Monday", "ODL1"),
                EntityTimetable(0, "ECE648", "Special topics", "EE241C8A", "EE", "Fakulti Kejuruteraan Elektrikal", "9:00 AM", "11:00 AM", "Monday", "ODL1"),
        )
        val fixNoonDataArray: ArrayList<EntityTimetable> = arrayListOf(
                EntityTimetable(0, "-", "-", "-", "-", "-", "12:00 am", "2:00 pm", "-", "-"),
                EntityTimetable(0, "-", "-", "-", "-", "-", "10:00 am", "12:00 am", "-", "-"),
        )
        val fix12hFormatDataArray: ArrayList<EntityTimetable> = arrayListOf(
                EntityTimetable(0, "-", "-", "-", "-", "-", "13:00 pm", "2:00 pm", "-", "-"),
                EntityTimetable(0, "-", "-", "-", "-", "-", "11:00 am", "13:00 pm", "-", "-"),
        )
        val fix12hFormatCaseDataArray: ArrayList<EntityTimetable> = arrayListOf(
                EntityTimetable(0, "-", "-", "-", "-", "-", "13:00 PM", "2:00 PM", "-", "-"),
                EntityTimetable(0, "-", "-", "-", "-", "-", "11:00 PM", "13:00 PM", "-", "-"),
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
        val nonOverlapCase: ArrayList<EntityTimetable> = arrayListOf(
                // same day
                EntityTimetable(0, "-", "-", "-", "-", "-", "8:00 AM", "9:00 AM", "Monday", "-"),
                EntityTimetable(0, "-", "-", "-", "-", "-", "9:00 AM", "10:00 AM", "Monday", "-"),
                // different day
                EntityTimetable(0, "-", "-", "-", "-", "-", "8:00 AM", "10:00 AM", "Tuesday", "-"),
                EntityTimetable(0, "-", "-", "-", "-", "-", "9:00 AM", "1`:A00 AM", "Wednesday", "-"),
        )
        val overlapCase: ArrayList<EntityTimetable> = arrayListOf(
                // same day
                EntityTimetable(0, "-", "-", "-", "-", "-", "8:00 AM", "10:00 AM", "Monday", "-"),
                EntityTimetable(0, "-", "-", "-", "-", "-", "9:00 AM", "11:00 AM", "Monday", "-"),
        )
    }
}