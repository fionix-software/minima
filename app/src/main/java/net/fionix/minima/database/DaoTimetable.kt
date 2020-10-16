package net.fionix.minima.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import net.fionix.minima.model.EntityTimetable

@Dao
interface DaoTimetable {

    @Query("SELECT * FROM EntityTimetable")
    fun getList(): List<EntityTimetable>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(timetable: EntityTimetable)

}