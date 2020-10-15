package net.fionix.minima.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

abstract class Database : RoomDatabase() {

    @Volatile
    private var instance: Database? = null

    abstract fun courseDao(): DaoCourse
    abstract fun facultyDao(): DaoFaculty
    abstract fun timetableDao(): DaoTimetable

    fun getDatabase(context: Context): Database {
        if (instance != null) {
            return instance as Database
        }
        synchronized(this) {
            val tempInstance = Room.databaseBuilder(context.applicationContext, Database::class.java, "minima_db").build()
            instance = tempInstance
            return instance as Database
        }
    }
}