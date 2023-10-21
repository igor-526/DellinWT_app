package com.example.dlworkhelper.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class ContactDB(
    @ColumnInfo(name = "comment")
    val comment: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "phone")
    val phone: String,

    @ColumnInfo(name = "position")
    val position: String,

    @PrimaryKey (autoGenerate = true)
    val id: Int? = null)
