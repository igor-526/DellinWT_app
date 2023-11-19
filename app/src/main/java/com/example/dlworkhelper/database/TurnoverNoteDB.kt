package com.example.dlworkhelper.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "journal_turnover")
data class TurnoverNoteDB(
    @PrimaryKey(autoGenerate = false)
    val id: Int,

    @ColumnInfo(name = "date")
    val date: String,

    @ColumnInfo(name = "cash")
    val cash: Float,
)
