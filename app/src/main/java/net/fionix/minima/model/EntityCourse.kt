package net.fionix.minima.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity

@Entity
class EntityCourse(
        @Embedded
        val faculty: EntityFaculty,
        @ColumnInfo
        val courseCode: String,
        @ColumnInfo
        val courseGroup: String,
        @ColumnInfo
        val courseName: String,
)