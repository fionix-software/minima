package net.fionix.minima

import android.database.Cursor
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import net.fionix.minima.database.DaoTimetable
import net.fionix.minima.database.DatabaseMain
import net.fionix.minima.model.EntityTimetable
import net.fionix.minima.model.ModelCourse
import net.fionix.minima.test.TestData
import net.fionix.minima.util.UtilData
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class DatabaseUnitTest {

    private lateinit var daoTimetable: DaoTimetable
    private lateinit var databaseMain: DatabaseMain

    @Before
    fun createDb() {
        databaseMain = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), DatabaseMain::class.java).build()
        daoTimetable = databaseMain.timetableDao()
    }

    @After
    fun closeDb() {
        databaseMain.close()
    }

    @Test
    fun insertData() {
        assert(daoTimetable.getList().isEmpty())
        daoTimetable.insert(TestData.databaseDataArray[0])
        assert(daoTimetable.getList().size == 1)
    }

    @Test
    fun insertMultipleData() {
        assert(daoTimetable.getList().isEmpty())
        for (data in TestData.databaseDataArray) {
            daoTimetable.insert(data)
        }
        assert(daoTimetable.getList().size == 6)
    }

    @Test
    fun insertMultipleDataDuplicate() {
        assert(daoTimetable.getList().isEmpty())
        for (data in TestData.databaseDuplicateDataArray) {
            daoTimetable.insert(data)
        }
        assert(daoTimetable.getList().size == 1)
    }

    @Test
    fun clearData() {
        assert(daoTimetable.getList().isEmpty())
        for (data in TestData.databaseDataArray) {
            daoTimetable.insert(data)
        }
        assert(daoTimetable.getList().size == 6)
        daoTimetable.clearData()
        assert(daoTimetable.getList().isEmpty())
    }

    @Test
    fun getCourseList() {
        assert(daoTimetable.getList().isEmpty())
        for (data in TestData.databaseDataArray) {
            daoTimetable.insert(data)
        }
        assert(daoTimetable.getList().size == 6)
        val cursor: Cursor = daoTimetable.getCourseList()
        val courseList: ArrayList<ModelCourse> = UtilData.cursorToCourseList(cursor)
        cursor.close()
        assert(courseList.size == 4)
        assert(courseList[0].courseCode == "ECE643")
        assert(courseList[1].courseCode == "ECE648")
        assert(courseList[2].courseCode == "ELE607")
        assert(courseList[3].courseCode == "ELE672")
    }

    @Test
    fun updateCourseName() {
        assert(daoTimetable.getList().isEmpty())
        for (data in TestData.databaseDataArray) {
            daoTimetable.insert(data)
        }
        assert(daoTimetable.getList().size == 6)
        val cursor1: Cursor = daoTimetable.getCourseList()
        val courseList: ArrayList<ModelCourse> = UtilData.cursorToCourseList(cursor1)
        cursor1.close()
        assert(courseList.size == 4)
        assert(courseList[0].courseName == "Network security")
        assert(courseList[1].courseName == "Special topics")
        assert(courseList[2].courseName == "EE Project 2")
        assert(courseList[3].courseName == "Industrial Topic")
        daoTimetable.updateCourseName("Special topics (Repeat)", courseList[1].courseCode, courseList[1].courseName, courseList[1].facultyCode)
        courseList.clear()
        assert(courseList.size == 0)
        val cursor2: Cursor = daoTimetable.getCourseList()
        courseList.addAll(UtilData.cursorToCourseList(cursor2))
        cursor2.close()
        assert(courseList.size == 4)
        assert(courseList[0].courseName == "Network security")
        assert(courseList[1].courseName == "Special topics (Repeat)")
        assert(courseList[2].courseName == "EE Project 2")
        assert(courseList[3].courseName == "Industrial Topic")
    }

    @Test
    fun deleteByCourse() {
        assert(daoTimetable.getList().isEmpty())
        for (data in TestData.databaseDataArray) {
            daoTimetable.insert(data)
        }
        assert(daoTimetable.getList().size == 6)
        val cursor1: Cursor = daoTimetable.getCourseList()
        val courseList: ArrayList<ModelCourse> = UtilData.cursorToCourseList(cursor1)
        cursor1.close()
        assert(courseList.size == 4)
        assert(courseList[0].courseName == "Network security")
        assert(courseList[1].courseName == "Special topics")
        assert(courseList[2].courseName == "EE Project 2")
        assert(courseList[3].courseName == "Industrial Topic")
        daoTimetable.deleteByCourse(courseList[1].courseCode, courseList[1].courseName, courseList[1].courseGroup, courseList[1].facultyCode, courseList[1].facultyName)
        courseList.clear()
        assert(courseList.size == 0)
        val cursor2: Cursor = daoTimetable.getCourseList()
        courseList.addAll(UtilData.cursorToCourseList(cursor2))
        cursor2.close()
        assert(courseList.size == 3)
        assert(courseList[0].courseName == "Network security")
        assert(courseList[1].courseName == "EE Project 2")
        assert(courseList[2].courseName == "Industrial Topic")
    }

    @Test
    fun getListByDay() {
        assert(daoTimetable.getList().isEmpty())
        for (data in TestData.databaseDataArray) {
            daoTimetable.insert(data)
        }
        assert(daoTimetable.getListByDay("Monday").size == 1)
        assert(daoTimetable.getListByDay("Tuesday").size == 2)
        assert(daoTimetable.getListByDay("Wednesday").isEmpty())
        assert(daoTimetable.getListByDay("Thursday").size == 2)
        assert(daoTimetable.getListByDay("Friday").size == 1)
    }

    @Test
    fun updateVenue() {
        assert(daoTimetable.getList().isEmpty())
        for (data in TestData.databaseDataArray) {
            daoTimetable.insert(data)
        }
        assert(daoTimetable.getList().size == 6)
        val timetableList: MutableList<EntityTimetable> = daoTimetable.getList().toMutableList()
        assert(timetableList[0].timetableVenue == "ODL1")
        assert(timetableList[1].timetableVenue == "ODL1")
        assert(timetableList[2].timetableVenue == "ODL1")
        assert(timetableList[3].timetableVenue == "ODL1")
        assert(timetableList[4].timetableVenue == "ODL1")
        assert(timetableList[5].timetableVenue == "ODL1")
        daoTimetable.updateVenue("ODL1a", timetableList[1].timetableId)
        timetableList.clear()
        timetableList.addAll(daoTimetable.getList().toMutableList())
        assert(timetableList[0].timetableVenue == "ODL1")
        assert(timetableList[1].timetableVenue == "ODL1a")
        assert(timetableList[2].timetableVenue == "ODL1")
        assert(timetableList[3].timetableVenue == "ODL1")
        assert(timetableList[4].timetableVenue == "ODL1")
        assert(timetableList[5].timetableVenue == "ODL1")
    }
}