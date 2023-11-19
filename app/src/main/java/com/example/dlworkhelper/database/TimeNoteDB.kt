package com.example.dlworkhelper.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "journal_time")
data class TimeNoteDB(
    @PrimaryKey(autoGenerate = false)
    val id: Int,

    @ColumnInfo(name = "start")
    val start: String,

    @ColumnInfo(name = "end")
    val end: String,

    @ColumnInfo(name = "total")
    val total: Float,

    @ColumnInfo(name = "c")
    val c: Int

)
