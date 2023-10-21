package com.example.dlworkhelper.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database (entities = [ContactDB::class,
                      SettingsDB::class], version = 2)
abstract class MainDB : RoomDatabase() {

    abstract fun getContactsDAO(): ContactsDAO

    abstract fun getSettingsDAO(): SettingsDAO

    companion object{
        fun getDB(context: Context): MainDB {
            return Room.databaseBuilder(
                context.applicationContext,
                MainDB::class.java,
                "dl.db").build()
        }
    }
}