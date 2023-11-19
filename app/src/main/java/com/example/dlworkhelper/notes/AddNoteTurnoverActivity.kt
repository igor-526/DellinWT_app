package com.example.dlworkhelper.notes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.example.dlworkhelper.DateFunc
import com.example.dlworkhelper.R
import com.example.dlworkhelper.database.MainDB
import com.example.dlworkhelper.databinding.ActivityAddNoteTurnoverBinding
import com.example.dlworkhelper.retrofit.JournalApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

class AddNoteTurnoverActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNoteTurnoverBinding
    private lateinit var db: MainDB
    private lateinit var retrofit: Retrofit
    private lateinit var date: LocalDate
    private var token = ""
    private var cash = 0f
    private var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteTurnoverBinding.inflate(layoutInflater)
        db = MainDB.getDB(this@AddNoteTurnoverActivity)
        retrofit = Retrofit.Builder().baseUrl("http://80.87.192.255:5000")
            .addConverterFactory(GsonConverterFactory.create()).build()
        CoroutineScope(Dispatchers.IO).launch {
            token = db.getSettingsDAO().getSettingValue("auth_token")
        }
        id = intent.getIntExtra("id", 0)
        if (id == 0){
            date = LocalDate.now()
        } else {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            date = LocalDate.parse(intent.getStringExtra("date"), formatter)
            cash = intent.getFloatExtra("cash", 0f)
            binding.TurnoverNoteAddTurnover.text = Editable.Factory.getInstance()
                .newEditable(cash.toString())
        }
        val dpd = DateFunc().getDatePickerDialog(this@AddNoteTurnoverActivity)
        dpd.datePicker.init(date.year, date.monthValue-1, date.dayOfMonth
        ){_, year, month, day -> run {
            date = LocalDate.of(year, month+1, day)
            binding.TurnoverNoteAddDatePickerText.text = DateFunc().dateToText(date)
        }}

        with(binding){
            TurnoverNoteAddTurnover.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.toString() == "" || s.toString().toFloat() < 1){
                        TurnoverNoteAddButtonSave.isClickable = false
                        TurnoverNoteAddButtonSave.setBackgroundColor(getColor(R.color.contacts_phone))
                    } else {
                        TurnoverNoteAddButtonSave.isClickable = true
                        TurnoverNoteAddButtonSave.setBackgroundColor(getColor(R.color.dl_dark))
                        cash = s.toString().toFloat()
                    }
                }
                override fun afterTextChanged(s: Editable?) {}
            })
            TurnoverNoteAddButtonSave.setOnClickListener {
                if (id == 0)
                    addNote()
                else
                    changeNote()
            }
            TurnoverNoteAddDatePickerText.text = DateFunc().dateToText(date)
            TurnoverNoteAddDatePickerButton.setOnClickListener {
                dpd.show()
            }
            TurnoverNoteAddButtonBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }

        setContentView(binding.root)
    }

    private fun addNote(){
        val journalApi = retrofit.create(JournalApi::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            val status = journalApi.addTurnover(token = token,
                date = date.toString(),
                cash = cash)
            runOnUiThread {
                if (status.status == "ok"){
                    Toast.makeText(this@AddNoteTurnoverActivity,
                        "Запись успешно добавлена!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@AddNoteTurnoverActivity,
                        "Ошибка! Запись не добавлена!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    private fun changeNote(){
        val journalApi = retrofit.create(JournalApi::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            val status = journalApi.changeTurnover(token = token,
                date = date.toString(),
                cash = cash,
                id = id)
            runOnUiThread {
                if (status.status == "ok"){
                    Toast.makeText(this@AddNoteTurnoverActivity,
                        "Запись успешно изменена!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@AddNoteTurnoverActivity,
                        "Ошибка! Запись не изменена!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}