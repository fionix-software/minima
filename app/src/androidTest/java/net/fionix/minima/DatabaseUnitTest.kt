package net.fionix.minima

import android.content.Context
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
import java.io.IOException

@RunWith(AndroidJUnit4ClassRunner::class)
class DatabaseUnitTest {

    private lateinit var daoTimetable: DaoTimetable
    private lateinit var databaseMain: DatabaseMain

    @Before
    fun createDb() {
        databaseMain = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext<Context>(), DatabaseMain::class.java).build()
        daoTimetable = databaseMain.timetableDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        databaseMain.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertData() {
        assert(daoTimetable.getList().isEmpty())
        daoTimetable.insert(TestData.databaseDataArray[0])
        assert(daoTimetable.getList().size == 1)
    }

    @Test
    @Throws(Exception::class)
    fun insertMultipleData() {
        assert(daoTimetable.getList().isEmpty())
        for (data in TestData.databaseDataArray) {
            daoTimetable.insert(data)
        }
        assert(daoTimetable.getList().size == 5)
    }

    @Test
    @Throws(Exception::class)
    fun clearData() {
        assert(daoTimetable.getList().isEmpty())
        for (data in TestData.databaseDataArray) {
            daoTimetable.insert(data)
        }
        assert(daoTimetable.getList().size == 5)
        daoTimetable.clearData()
        assert(daoTimetable.getList().isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun getCourseList() {
        assert(daoTimetable.getList().isEmpty())
        for (data in TestData.databaseDataArray) {
            daoTimetable.insert(data)
        }
        assert(daoTimetable.getList().size == 5)
        val cursor: Cursor = daoTimetable.getCourseList()
        val courseList: ArrayList<ModelCourse> = UtilData.cursorToCourseList(cursor)
        cursor.close()
        assert(courseList.size == 2)
        assert(courseList[0].courseName == "AAA")
        assert(courseList[1].courseName == "BBB")
    }

    @Test
    @Throws(Exception::class)
    fun update() {
        assert(daoTimetable.getList().isEmpty())
        for (data in TestData.databaseDataArray) {
            daoTimetable.insert(data)
        }
        assert(daoTimetable.getList().size == 5)
        val cursor1: Cursor = daoTimetable.getCourseList()
        val courseList: ArrayList<ModelCourse> = UtilData.cursorToCourseList(cursor1)
        cursor1.close()
        assert(courseList.size == 2)
        assert(courseList[0].courseName == "AAA")
        assert(courseList[1].courseName == "BBB")
        daoTimetable.updateCourseName("CCC", courseList[1].courseCode, courseList[1].courseName, courseList[1].facultyCode)
        courseList.clear()
        assert(courseList.size == 0)
        val cursor2: Cursor = daoTimetable.getCourseList()
        courseList.addAll(UtilData.cursorToCourseList(cursor2))
        cursor2.close()
        assert(courseList.size == 2)
        assert(courseList[0].courseName == "AAA")
        assert(courseList[1].courseName == "CCC")
    }

    @Test
    @Throws(Exception::class)
    fun deleteByCourse() {
        assert(daoTimetable.getList().isEmpty())
        for (data in TestData.databaseDataArray) {
            daoTimetable.insert(data)
        }
        assert(daoTimetable.getList().size == 5)
        val cursor1: Cursor = daoTimetable.getCourseList()
        val courseList: ArrayList<ModelCourse> = UtilData.cursorToCourseList(cursor1)
        cursor1.close()
        assert(courseList.size == 2)
        assert(courseList[0].courseName == "AAA")
        assert(courseList[1].courseName == "BBB")
        daoTimetable.deleteByCourse(courseList[1].courseCode, courseList[1].courseName, courseList[1].courseGroup, courseList[1].facultyCode, courseList[1].facultyName)
        courseList.clear()
        assert(courseList.size == 0)
        val cursor2: Cursor = daoTimetable.getCourseList()
        courseList.addAll(UtilData.cursorToCourseList(cursor2))
        cursor2.close()
        assert(courseList.size == 1)
        assert(courseList[0].courseName == "AAA")
    }

    @Test
    @Throws(Exception::class)
    fun getListByDay() {
        assert(daoTimetable.getList().isEmpty())
        for (data in TestData.databaseDataArray) {
            daoTimetable.insert(data)
        }
        assert(daoTimetable.getListByDay("Monday").size == 1)
        assert(daoTimetable.getListByDay("Tuesday").size == 1)
        assert(daoTimetable.getListByDay("Wednesday").size == 1)
        assert(daoTimetable.getListByDay("Thursday").isEmpty())
        assert(daoTimetable.getListByDay("Friday").size == 2)
    }

    @Test
    @Throws(Exception::class)
    fun updateVenue() {
        assert(daoTimetable.getList().isEmpty())
        for (data in TestData.databaseDataArray) {
            daoTimetable.insert(data)
        }
        assert(daoTimetable.getList().size == 5)
        val timetableList: MutableList<EntityTimetable> = daoTimetable.getList().toMutableList()
        assert(timetableList[0].courseName == "AAA")
        assert(timetableList[0].timetableVenue == "Venue 1")
        assert(timetableList[1].courseName == "AAA")
        assert(timetableList[1].timetableVenue == "Venue 1")
        assert(timetableList[2].courseName == "AAA")
        assert(timetableList[2].timetableVenue == "Venue 2")
        assert(timetableList[3].courseName == "BBB")
        assert(timetableList[3].timetableVenue == "Venue 1")
        assert(timetableList[4].courseName == "BBB")
        assert(timetableList[4].timetableVenue == "Venue 3")
        daoTimetable.updateVenue("Venue 4", timetableList[1].timetableId)
        timetableList.clear()
        timetableList.addAll(daoTimetable.getList().toMutableList())
        assert(timetableList[0].courseName == "AAA")
        assert(timetableList[0].timetableVenue == "Venue 1")
        assert(timetableList[1].courseName == "AAA")
        assert(timetableList[1].timetableVenue == "Venue 4")
        assert(timetableList[2].courseName == "AAA")
        assert(timetableList[2].timetableVenue == "Venue 2")
        assert(timetableList[3].courseName == "BBB")
        assert(timetableList[3].timetableVenue == "Venue 1")
        assert(timetableList[4].courseName == "BBB")
        assert(timetableList[4].timetableVenue == "Venue 3")
    }
}