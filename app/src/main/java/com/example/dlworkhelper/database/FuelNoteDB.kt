package com.example.dlworkhelper.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "journal_fuel")
data class FuelNoteDB(
    @PrimaryKey(autoGenerate = false)
    val id: Int,

    @ColumnInfo(name = "date")
    val date: String,

    @ColumnInfo(name = "fuel")
    val fuel: Float,

    @ColumnInfo(name = "km")
    val km: Int,
)
