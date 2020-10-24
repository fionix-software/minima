package net.fionix.minima.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import net.fionix.minima.model.EntityTimetable

@Database(entities = [EntityTimetable::class], version = 1, exportSchema = false)
abstract class DatabaseMain : RoomDatabase() {

    abstract fun timetableDao(): DaoTimetable

    companion object {

        @Volatile
        private var instance: DatabaseMain? = null

        fun getDatabase(context: Context): DatabaseMain {
            val instance = this.instance
            if (instance != null) {
                return instance
            }
            synchronized(this) {
                val tempInstance = Room.databaseBuilder(context.applicationContext, DatabaseMain::class.java, "minima_db").build()
                this.instance = tempInstance
                return tempInstance
            }
        }
    }
}