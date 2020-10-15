package net.fionix.minima.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity
class EntityFaculty(
        @ColumnInfo
        val facultyCode: String,
        @ColumnInfo
        val facultyDescription: String,
)