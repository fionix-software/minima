package net.fionix.minima.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity

@Entity
class EntityTimetable(
        @Embedded
        val course: EntityCourse,
        @ColumnInfo
        val timeStart: String,
        @ColumnInfo
        val timeEnd: String,
        @ColumnInfo
        val day: String,
        @ColumnInfo
        val venue: String,
)