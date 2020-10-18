package net.fionix.minima

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import net.fionix.minima.database.DaoTimetable
import net.fionix.minima.database.DatabaseMain
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
        assert(daoTimetable.getList().size == 2)
    }


}