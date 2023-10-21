package com.example.dlworkhelper.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "settings")
data class SettingsDB(
    @PrimaryKey
    val name: String,

    @ColumnInfo
    val value: String

)
