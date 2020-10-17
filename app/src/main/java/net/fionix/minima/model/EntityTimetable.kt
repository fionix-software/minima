package net.fionix.minima.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EntityTimetable(
        @PrimaryKey(autoGenerate = true)
        var timetableId: Int,
        @ColumnInfo
        override var courseCode: String,
        @ColumnInfo
        override var courseName: String,
        @ColumnInfo
        override var courseGroup: String,
        @ColumnInfo
        override var facultyCode: String,
        @ColumnInfo
        override var facultyName: String,
        @ColumnInfo
        var timetableTimeStart: String,
        @ColumnInfo
        var timetableTimeEnd: String,
        @ColumnInfo
        var timetableDay: String,
        @ColumnInfo
        var timetableVenue: String,
) : IModelCourse