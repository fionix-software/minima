package net.fionix.minima.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class EntityCourse(
        @PrimaryKey(autoGenerate = true)
        val courseId: Int,
        @Embedded
        val faculty: EntityFaculty,
        @ColumnInfo
        val courseCode: String,
        @ColumnInfo
        val courseGroup: String,
)