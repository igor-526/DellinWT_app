package com.example.dlworkhelper.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "auto")
data class AutoDB(
    @PrimaryKey(autoGenerate = false)
    val id: Int? = null,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "consumption")
    val consumption: Float,

    @ColumnInfo(name = "tank")
    val tank: Int
)
