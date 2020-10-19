package net.fionix.minima

import android.content.Context
import android.database.Cursor
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import net.fionix.minima.database.DaoTimetable
import net.fionix.minima.database.DatabaseMain
import net.fionix.minima.model.ModelCourse
import net.fionix.minima.test.TestData
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
        val courseList: ArrayList<ModelCourse> = arrayListOf()
        cursor.use { c ->
            while (c.moveToNext()) {
                // column 0: course code
                // column 1: course name
                // column 2: course group
                // column 3: faculty code
                // column 4: faculty name
                courseList.add(ModelCourse(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4)))
            }
        }
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
        val courseList: ArrayList<ModelCourse> = arrayListOf()
        cursor1.use { c ->
            while (c.moveToNext()) {
                // column 0: course code
                // column 1: course name
                // column 2: course group
                // column 3: faculty code
                // column 4: faculty name
                courseList.add(ModelCourse(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4)))
            }
        }
        assert(courseList.size == 2)
        assert(courseList[0].courseName == "AAA")
        assert(courseList[1].courseName == "BBB")
        daoTimetable.updateCourseName("CCC", courseList[1].courseCode, courseList[1].courseName, courseList[1].facultyCode)
        courseList.clear()
        assert(courseList.size == 0)
        val cursor2: Cursor = daoTimetable.getCourseList()
        cursor2.use { c ->
            while (c.moveToNext()) {
                // column 0: course code
                // column 1: course name
                // column 2: course group
                // column 3: faculty code
                // column 4: faculty name
                courseList.add(ModelCourse(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4)))
            }
        }
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
        val courseList: ArrayList<ModelCourse> = arrayListOf()
        cursor1.use { c ->
            while (c.moveToNext()) {
                // column 0: course code
                // column 1: course name
                // column 2: course group
                // column 3: faculty code
                // column 4: faculty name
                courseList.add(ModelCourse(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4)))
            }
        }
        assert(courseList.size == 2)
        assert(courseList[0].courseName == "AAA")
        assert(courseList[1].courseName == "BBB")
        daoTimetable.deleteByCourse(courseList[1].courseCode, courseList[1].courseName, courseList[1].courseGroup, courseList[1].facultyCode, courseList[1].facultyName)
        courseList.clear()
        assert(courseList.size == 0)
        val cursor2: Cursor = daoTimetable.getCourseList()
        cursor2.use { c ->
            while (c.moveToNext()) {
                // column 0: course code
                // column 1: course name
                // column 2: course group
                // column 3: faculty code
                // column 4: faculty name
                courseList.add(ModelCourse(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4)))
            }
        }
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
}