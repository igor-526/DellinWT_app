package com.example.dlworkhelper.notes

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.dlworkhelper.DateFunc
import com.example.dlworkhelper.R
import com.example.dlworkhelper.database.TimeNoteDB
import com.example.dlworkhelper.databinding.ActivityAddNoteTimeBinding
import com.example.dlworkhelper.mainApp.FragmentMenuJournal
import com.example.dlworkhelper.mainApp.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class AddNoteTimeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNoteTimeBinding
    private lateinit var date: LocalDate
    private lateinit var timeStart: LocalTime
    private lateinit var timeEnd: LocalTime
    private var c = 1
    private var total = 9f
    private var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteTimeBinding.inflate(layoutInflater)
        id = intent.getIntExtra("id", 0)
        if (id == 0){
            date = LocalDate.now()
            timeStart = LocalTime.of(8,0)
            timeEnd = LocalTime.of(18,0)
        } else {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            date = LocalDate.parse(intent.getStringExtra("start"), formatter)
            timeStart = LocalTime.parse(intent.getStringExtra("start"), formatter)
            timeEnd = LocalTime.parse(intent.getStringExtra("end"), formatter)
            if (intent.getIntExtra("c", 1) == 1){
                c = 1
                binding.TimeNoteAddCheckGroup.check(binding.TimeNoteAddCheckGroupWorkDay.id)
            } else {
                c = 2
                binding.TimeNoteAddCheckGroup.check(binding.TimeNoteAddCheckGroupDayOff.id)
            }
            showTotal()
        }
        val dpd = DateFunc().getDatePickerDialog(this@AddNoteTimeActivity)
        dpd.datePicker.init(date.year, date.monthValue-1, date.dayOfMonth
        ){_, year, month, day -> run {
            date = LocalDate.of(year, month+1, day)
            binding.TimeNoteAddDatePickerText.text = DateFunc().dateToText(date)
        }}
        with (binding) {
            TimeNoteAddStartPicker.setIs24HourView(true)
            TimeNoteAddEndPicker.setIs24HourView(true)
            TimeNoteAddStartPicker.hour = timeStart.hour
            TimeNoteAddStartPicker.minute = timeStart.minute
            TimeNoteAddEndPicker.hour = timeEnd.hour
            TimeNoteAddEndPicker.minute = timeEnd.minute
            TimeNoteAddDatePickerText.text = DateFunc().dateToText(date)
            TimeNoteAddStartPicker.setOnTimeChangedListener { _, hourOfDay, minute ->
                timeStart = LocalTime.of(hourOfDay, minute)
                showTotal()
            }
            TimeNoteAddEndPicker.setOnTimeChangedListener { _, hourOfDay, minute ->
                timeEnd = LocalTime.of(hourOfDay, minute)
                showTotal()
            }
            TimeNoteAddCheckGroupWorkDay.setOnClickListener {
                c = 1
                showTotal()
            }
            TimeNoteAddCheckGroupDayOff.setOnClickListener {
                c = 2
                showTotal()
            }
            TimeNoteAddButtonSave.setOnClickListener {
                if (id == 0)
                    addNote()
                else
                    changeNote()
            }
            TimeNoteAddDatePickerButton.setOnClickListener {
                dpd.show()
            }
            TimeNoteAddButtonBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }


        }
        showTotal()
        setContentView(binding.root)
    }

    private fun showError(){
        with (binding) {
            TimeNoteAddError.text = R.string.NoteAddTimeError.toString()
            TimeNoteAddError.visibility = View.VISIBLE
            TimeNoteAddButtonSave.isClickable = false
            TimeNoteAddButtonSave.setBackgroundColor(Color.GRAY)
        }
    }

    private fun deleteError(){
        with (binding) {
            TimeNoteAddError.visibility = View.GONE
            TimeNoteAddButtonSave.isClickable = true
            TimeNoteAddButtonSave.setBackgroundColor(getColor(R.color.dl_dark))
        }
    }

    private fun showTotal(){
        if (timeStart < timeEnd){
            if (binding.TimeNoteAddError.visibility == View.VISIBLE)
                deleteError()
            with (binding) {
                val duration = Duration.between(timeStart, timeEnd)
                var dinner = 0.0
                if (duration.toHours() > 4)
                    dinner = 0.5
                if (duration.toHours() > 6)
                    dinner = 1.0
                val durationTotal = Duration.between(timeStart, timeEnd).minusHours(dinner.toLong()).multipliedBy(c.toLong())
                val totalTimeStr = durationTotal.toHours().toString().padStart(2, '0') +
                        ":${(durationTotal.toMinutes()%60).toString().padStart(2, '0')} ч."
                val totalDinnerStr = "$dinner ч."
                val totalWorkStr = duration.toHours().toString().padStart(2, '0') +
                        ":${(duration.toMinutes()%60).toString().padStart(2, '0')} ч."
                total = durationTotal.toMinutes().toFloat()/60f
                TimeNoteAddTotalTime.text = totalTimeStr
                TimeNoteAddTotalDinner.text = totalDinnerStr
                TimeNoteAddTotalWork.text = totalWorkStr
            }
        } else {
            showError()
        }
    }

    private fun addNote(){
        val startStr = "$date $timeStart"
        val endStr = "$date $timeEnd"
        CoroutineScope(Dispatchers.IO).launch {
            val status = MainActivity.journalAPI.addTime(token = MainActivity.token,
                start = startStr,
                end = endStr,
                c = c,
                total = total)
            runOnUiThread {
                if (status.status != "error"){
                    CoroutineScope(Dispatchers.IO).launch {
                        MainActivity.db.getJournalDAO().insertTimeNote(
                            TimeNoteDB(
                            id = status.status.toInt(),
                            start = startStr+":00",
                            end = endStr+":00",
                            total = (total*100).toInt()/100f,
                            c = c
                        )
                        )
                        val newList = MainActivity.db.getJournalDAO().getAllTime()
                        runOnUiThread {
                            FragmentMenuJournal.timeAdapter.submitList(newList)
                            Toast.makeText(this@AddNoteTimeActivity,
                                "Запись успешно добавлена!", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }

                } else {
                    Toast.makeText(this@AddNoteTimeActivity,
                        "Ошибка! Запись не добавлена!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    private fun changeNote(){
        val startStr = "$date $timeStart"
        val endStr = "$date $timeEnd"
        CoroutineScope(Dispatchers.IO).launch {
            val status = MainActivity.journalAPI.changeTime(token = MainActivity.token,
                start = startStr,
                end = endStr,
                c = c,
                total = total,
                id = id)
            runOnUiThread {
                if (status.status == "ok"){
                    CoroutineScope(Dispatchers.IO).launch {
                        MainActivity.db.getJournalDAO().changeTime(
                            id = id,
                            start = startStr+":00",
                            end = endStr+":00",
                            c = c,
                            total = (total*100).toInt()/100f
                        )
                        val newList = MainActivity.db.getJournalDAO().getAllTime()
                        runOnUiThread {
                            FragmentMenuJournal.timeAdapter.submitList(newList)
                            Toast.makeText(this@AddNoteTimeActivity,
                                "Запись успешно обновлена!", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                } else {
                    Toast.makeText(this@AddNoteTimeActivity,
                        "Ошибка! Запись не обновлена!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}