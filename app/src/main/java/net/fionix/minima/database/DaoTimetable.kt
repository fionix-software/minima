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

    @Query("SELECT * FROM EntityTimetable WHERE timetableDay=:day")
    fun getListByDay(day: String): List<EntityTimetable>

    @Query("SELECT DISTINCT courseCode, courseName, courseGroup, facultyCode, facultyName FROM EntityTimetable")
    fun getCourseList(): Cursor

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(timetable: EntityTimetable)

    @Query("UPDATE EntityTimetable SET courseName=:newCourseName WHERE courseCode=:courseCode AND courseName=:oldCourseName AND facultyCode=:facultyCode")
    fun updateCourseName(newCourseName: String, courseCode: String, oldCourseName: String, facultyCode: String)

    @Query("UPDATE EntityTimetable SET timetableVenue=:newTimetableVenue WHERE timetableId=:timetableId")
    fun updateVenue(newTimetableVenue: String, timetableId: Int)

    @Query("DELETE FROM EntityTimetable WHERE courseCode=:courseCode AND courseName=:courseName AND courseGroup=:courseGroup AND facultyCode=:facultyCode AND facultyName=:facultyName")
    fun deleteByCourse(courseCode: String, courseName: String, courseGroup: String, facultyCode: String, facultyName: String)

    @Query("DELETE FROM EntityTimetable")
    fun clearData()

}