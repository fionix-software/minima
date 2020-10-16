package net.fionix.minima.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EntityTimetable(
        @PrimaryKey(autoGenerate = true)
        val timetableId: Int,
        @ColumnInfo
        override val courseCode: String,
        @ColumnInfo
        override val courseGroup: String,
        @ColumnInfo
        override val facultyCode: String,
        @ColumnInfo
        override val facultyName: String,
        @ColumnInfo
        val timetableTimeStart: String,
        @ColumnInfo
        val timetableTimeEnd: String,
        @ColumnInfo
        val timetableDay: String,
        @ColumnInfo
        val timetableVenue: String,
) : IModelCourse