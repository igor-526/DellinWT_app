package com.example.dlworkhelper.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.dlworkhelper.dataclasses.TimeNote

@Dao
interface JournalDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTimeNote(note: TimeNoteDB)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFuelNote(note: FuelNoteDB)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTurnoverNote(note: TurnoverNoteDB)

    @Query("SELECT * FROM journal_time")
    fun getAllTime(): List<TimeNoteDB>

    @Query("SELECT * FROM journal_fuel")
    fun getAllFuel(): List<FuelNoteDB>

    @Query("SELECT * FROM journal_turnover")
    fun getAllTurnover(): List<TurnoverNoteDB>

    @Query("DELETE FROM journal_time")
    fun deleteAllTime()

    @Query("DELETE FROM journal_fuel")
    fun deleteAllFuel()

    @Query("DELETE FROM journal_turnover")
    fun deleteAllTurnover()

    @Query("DELETE FROM journal_time WHERE id=:id")
    fun deleteTime(id: Int)

    @Query("UPDATE journal_time SET start=:start, 'end'=:end, total=:total, c=:c WHERE id=:id")
    fun changeTime(id: Int,
                   start: String,
                   end: String,
                   total: Float,
                   c: Int)


}