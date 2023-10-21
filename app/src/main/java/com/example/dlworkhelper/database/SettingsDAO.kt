package com.example.dlworkhelper.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SettingsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSetting(setting: SettingsDB)

    @Query("SELECT value FROM settings WHERE name = :name LIMIT 1")
    fun getSettingValue(name: String): String

    @Query("DELETE FROM settings WHERE name = :name")
    fun deleteSetting(name: String?)
}