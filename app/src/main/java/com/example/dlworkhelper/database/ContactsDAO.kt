package com.example.dlworkhelper.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ContactsDAO {
    @Insert
    fun insertContact(contact: ContactDB)

    @Query("SELECT * FROM contacts")
    fun getAllContacts(): List<ContactDB>

    @Query("DELETE FROM contacts")
    fun deleteAllContacts()
}