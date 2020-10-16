package net.fionix.minima.database

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import net.fionix.minima.model.EntityTimetable

@Dao
interface DaoTimetable {

    @Query("SELECT * FROM EntityTimetable")
    fun getList(): List<EntityTimetable>

    @Query("SELECT DISTINCT courseCode, courseGroup, facultyCode, facultyName FROM EntityTimetable")
    fun getCourseList(): Cursor

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(timetable: EntityTimetable)

}