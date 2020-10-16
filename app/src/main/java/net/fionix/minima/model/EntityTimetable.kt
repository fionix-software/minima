package net.fionix.minima.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class EntityTimetable(
        @PrimaryKey(autoGenerate = true)
        val timetableId: Int,
        @Embedded
        val timetableCourse: EntityCourse,
        @ColumnInfo
        val timetableTimeStart: String,
        @ColumnInfo
        val timetableTimeEnd: String,
        @ColumnInfo
        val timetableDay: String,
        @ColumnInfo
        val timetableVenue: String,
)