package net.fionix.minima.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class EntityFaculty(
        @PrimaryKey(autoGenerate = true)
        val facultyId: Int,
        @ColumnInfo
        val facultyCode: String,
        @ColumnInfo
        val facultyName: String,
)