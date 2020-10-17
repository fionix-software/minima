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

    @Query("SELECT DISTINCT courseCode, courseName, courseGroup, facultyCode, facultyName FROM EntityTimetable")
    fun getCourseList(): Cursor

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(timetable: EntityTimetable)

    @Query("UPDATE EntityTimetable SET courseName=:courseName WHERE courseCode=:courseCode AND courseGroup=:courseGroup AND facultyCode=:facultyCode AND facultyName=:facultyName")
    fun updateName(courseCode: String, courseName: String, courseGroup: String, facultyCode: String, facultyName: String)

    @Query("DELETE FROM EntityTimetable WHERE courseCode=:courseCode AND courseName=:courseName AND courseGroup=:courseGroup AND facultyCode=:facultyCode AND facultyName=:facultyName")
    fun deleteByCourse(courseCode: String, courseName: String, courseGroup: String, facultyCode: String, facultyName: String)

    @Query("DELETE FROM EntityTimetable")
    fun clearData()

}