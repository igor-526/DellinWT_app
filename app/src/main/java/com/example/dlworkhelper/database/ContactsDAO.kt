package com.example.dlworkhelper.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ContactsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertContact(contact: ContactDB)

    @Query("SELECT * FROM contacts")
    fun getAllContacts(): List<ContactDB>

    @Query("DELETE FROM contacts")
    fun deleteAllContacts()

    @Query("SELECT * FROM contacts WHERE position = :pos")
    fun getPositionContacts(pos: String): List<ContactDB>

    @Query("SELECT * FROM contacts WHERE name LIKE '%' || :q || '%'")
    fun getSearchContacts(q: String): List<ContactDB>
}