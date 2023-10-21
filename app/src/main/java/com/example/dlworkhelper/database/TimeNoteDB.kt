package com.example.dlworkhelper.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date
import java.sql.Time

@Entity(tableName = "journal_time")
data class TimeNoteDB(
    @PrimaryKey
    val id: Int,

    @ColumnInfo(name = "date")
    val date: Date,

    @ColumnInfo(name = "start")
    val start: Time,

    @ColumnInfo(name = "end")
    val end: Time,

    @ColumnInfo(name = "total")
    val total: Float,

    @ColumnInfo(name = "c")
    val c: Int

)
