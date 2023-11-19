package com.example.dlworkhelper.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AutoDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAuto(auto: AutoDB)

    @Query("SELECT name FROM auto")
    fun getAll(): List<String>

    @Query("DELETE FROM auto")
    fun deleteAll()

    @Query("SELECT * FROM auto WHERE name LIKE :q LIMIT 1")
    fun byName(q: String): AutoDB
}