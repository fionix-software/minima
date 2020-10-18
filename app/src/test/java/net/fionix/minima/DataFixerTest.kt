package net.fionix.minima

import net.fionix.minima.test.TestData
import net.fionix.minima.util.UtilDataFixer
import org.junit.Test

class DataFixerTest {

    @Test
    fun fixNoonDataFixerTest() {
        assert(TestData.fixNoonDataArray[0].timetableTimeStart == "12:00 am")
        assert(TestData.fixNoonDataArray[1].timetableTimeEnd == "12:00 am")
        val fixedData = UtilDataFixer.fixTimetable(TestData.fixNoonDataArray)
        assert(fixedData[0].timetableTimeStart == "12:00 pm")
        assert(fixedData[1].timetableTimeEnd == "12:00 pm")
    }

    @Test
    fun fix12hFormatDataFixerTest() {
        assert(TestData.fix12hFormatDataArray[0].timetableTimeStart == "13:00 pm")
        assert(TestData.fix12hFormatDataArray[1].timetableTimeEnd == "13:00 pm")
        val fixedData = UtilDataFixer.fixTimetable(TestData.fix12hFormatDataArray)
        assert(fixedData[0].timetableTimeStart == "1:00 pm")
        assert(fixedData[1].timetableTimeEnd == "1:00 pm")
    }

    @Test
    fun fixMinute15DataFixerTest() {
        assert(TestData.fixMinute15DataArray[0].timetableTimeStart == "1:5 pm")
        assert(TestData.fixMinute15DataArray[1].timetableTimeEnd == "2:5 pm")
        val fixedData = UtilDataFixer.fixTimetable(TestData.fixMinute15DataArray)
        assert(fixedData[0].timetableTimeStart == "1:15 pm")
        assert(fixedData[1].timetableTimeEnd == "2:15 pm")
    }

    @Test
    fun fixFacultyNameFixerTest() {
        assert(TestData.fixFacultyNameDataArray[0].facultyName == "(UiTM Kelantan [HEA/JW/05-2007)")
        assert(TestData.fixFacultyNameDataArray[1].facultyName == "PERLIS")
        assert(TestData.fixFacultyNameDataArray[2].facultyName == "Perlis")
        assert(TestData.fixFacultyNameDataArray[3].facultyName == "Cawangan  N. Sembilan")
        assert(TestData.fixFacultyNameDataArray[4].facultyName == "HEP SHAH ALAM")
        assert(TestData.fixFacultyNameDataArray[5].facultyName == "UiTM Kelantan [HEA/JW/05-2007]")
        assert(TestData.fixFacultyNameDataArray[6].facultyName == "PUSAT PEMIKIRAN DAN KEFAHAMAN ISLAM (CITU)")
        assert(TestData.fixFacultyNameDataArray[7].facultyName == "Kampus Kedah")
        assert(TestData.fixFacultyNameDataArray[8].facultyName == "KAMPUS PERAK")
        assert(TestData.fixFacultyNameDataArray[9].facultyName == "Kampus Shah Alam - Akademi Pengajian Bahasa")
        val fixedData = UtilDataFixer.fixTimetable(TestData.fixFacultyNameDataArray)
        assert(fixedData[0].facultyName == "UiTM Kelantan [HEA/JW/05-2007]")
        assert(fixedData[1].facultyName == "Kampus Perlis")
        assert(fixedData[2].facultyName == "Kampus Perlis")
        assert(fixedData[3].facultyName == "Cawangan N. Sembilan")
        assert(fixedData[4].facultyName == "HEP Shah Alam")
        assert(fixedData[5].facultyName == "UiTM Kelantan [HEA/JW/05-2007]")
        assert(fixedData[6].facultyName == "Pusat Pemikiran Dan Kefahaman Islam (CITU)")
        assert(fixedData[7].facultyName == "Kampus Kedah")
        assert(fixedData[8].facultyName == "Kampus Perak")
        assert(fixedData[9].facultyName == "Kampus Shah Alam - Akademi Pengajian Bahasa")
    }

}