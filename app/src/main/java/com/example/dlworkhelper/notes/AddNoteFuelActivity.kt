package com.example.dlworkhelper.notes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.dlworkhelper.DateFunc
import com.example.dlworkhelper.R
import com.example.dlworkhelper.database.MainDB
import com.example.dlworkhelper.databinding.ActivityAddNoteFuelBinding
import com.example.dlworkhelper.mainApp.FragmentMenuJournal
import com.example.dlworkhelper.retrofit.JournalApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.util.Calendar

class AddNoteFuelActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNoteFuelBinding
    private lateinit var db: MainDB
    private lateinit var retrofit: Retrofit
    private lateinit var date: LocalDate
    private var token = ""
    private var lastKm = 0
    private var lastF = 0f
    private var consumption = 14.7f
    private var tank = 80
    private var kmS = 0
    private var kmF = 0
    private var refuel = 0f
    private var fS = 0f
    private var fF = 0f
    private var fuelDelta = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = MainDB.getDB(this@AddNoteFuelActivity)
        binding = ActivityAddNoteFuelBinding.inflate(layoutInflater)
        date = LocalDate.now()

        val dpd = DateFunc().getDatePickerDialog(this@AddNoteFuelActivity)
        dpd.datePicker.init(date.year, date.monthValue-1, date.dayOfMonth
        ){_, year, month, day -> run {
            date = LocalDate.of(year, month+1, day)
            binding.FuelNoteAddDatePickerText.text = DateFunc().dateToText(date)
        }}

        val extra = intent.getIntExtra("note_id", 0)
        Toast.makeText(this@AddNoteFuelActivity, extra.toString(), Toast.LENGTH_SHORT).show()
        getAutos()
        getLast()
        retrofit = Retrofit.Builder().baseUrl("http://80.87.192.255:5000")
            .addConverterFactory(GsonConverterFactory.create()).build()
        CoroutineScope(Dispatchers.IO).launch {
            token = db.getSettingsDAO().getSettingValue("auth_token")
        }
        with(binding){
            FuelNoteAddButtonLast.setOnClickListener {
                setLast()
            }
            FuelNoteAddDatePickerText.text = DateFunc().dateToText(date)
            FuelNoteAddDataKmS.addTextChangedListener(object: TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.toString() != "") {
                        kmS = s.toString().toInt()
                        setTotal()
                    }
                }
                override fun afterTextChanged(s: Editable?) {}
            })
            FuelNoteAddDataKmF.addTextChangedListener(object: TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.toString() != ""){
                    kmF = s.toString().toInt()
                    setTotal()
                    }
                }
                override fun afterTextChanged(s: Editable?) {}
            })
            FuelNoteAddDataFuelS.addTextChangedListener(object: TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.toString() != ""){
                    fS = s.toString().toFloat()
                    setTotal()
                    }
                }
                override fun afterTextChanged(s: Editable?) {}
            })
            FuelNoteAddDataRefuel.addTextChangedListener(object: TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.toString() != ""){
                    refuel = s.toString().toFloat()
                    setTotal()
                    }
                }
                override fun afterTextChanged(s: Editable?) {}
            })
            FuelNoteAddButtonSave.setOnClickListener {
                addNote()
            }
            FuelNoteAddDatePickerButton.setOnClickListener {
                dpd.show()
            }
            FuelNoteAddButtonBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
        setContentView(binding.root)
    }

    private fun getLast(){
        CoroutineScope(Dispatchers.IO).launch {
            val lkm = db.getSettingsDAO().getSettingValue("last_km")
            val lf = db.getSettingsDAO().getSettingValue("last_fuel")
            runOnUiThread {
                if (lkm != "0"){
                    lastKm = lkm.toInt()
                    lastF = lf.toFloat()
                    with(binding){
                        FuelNoteAddButtonLast.isClickable = true
                        FuelNoteAddButtonLast.setBackgroundColor(getColor(R.color.dl_dark))
                    }
                }
            }
        }
    }

    private fun setLast(){
        with(binding){
            FuelNoteAddDataFuelS.text = Editable.Factory.getInstance().newEditable(lastF.toString())
            FuelNoteAddDataKmS.text = Editable.Factory.getInstance().newEditable(lastKm.toString())
        }
    }

    private fun setTotal(){
        val totalKM = kmF-kmS
        if (totalKM < 1){
            showError()
        } else {
            with(binding){
                if (FuelNoteAddError.visibility == View.VISIBLE)
                    deleteError()
                fuelDelta = totalKM / 100f * consumption
                val fuelF = fS - fuelDelta + refuel
                val totalKMText = "$totalKM км"
                if (fuelF < 1)
                    fF = 1f
                else if (fuelF > tank)
                    fF = tank.toFloat()
                else
                    fF = fuelF
                val totalFText = "$fF л."
                FuelNoteAddDataKmTotal.text = totalKMText
                FuelNoteAddDataFuelTotal.text = totalFText
            }
        }
    }

    private fun showError(){
        with(binding){
            FuelNoteAddError.text = "Ошибка!\nОдометр при прибытии не может быть меньше одометра при выезде!"
            FuelNoteAddError.visibility = View.VISIBLE
            FuelNoteAddButtonSave.isClickable = false
            FuelNoteAddButtonSave.setBackgroundColor(getColor(R.color.contacts_phone))
        }
    }

    private fun deleteError(){
        with(binding){
            FuelNoteAddError.visibility = View.GONE
            FuelNoteAddButtonSave.isClickable = true
            FuelNoteAddButtonSave.setBackgroundColor(getColor(R.color.dl_dark))
        }
    }

    private fun getAutos(){
        CoroutineScope(Dispatchers.IO).launch {
            val autos = db.getAutoDAO().getAll().toMutableList()
            runOnUiThread {
                val arrayAdapter = ArrayAdapter(this@AddNoteFuelActivity, android.R.layout.simple_spinner_dropdown_item, autos)
                binding.FuelNoteAddAutoSelect.adapter = arrayAdapter
                binding.FuelNoteAddAutoSelect.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        selectAuto(autos[position])
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}

                }
            }
        }
    }

    private fun selectAuto(name: String){
        CoroutineScope(Dispatchers.IO).launch {
            val auto = db.getAutoDAO().byName(name)
            runOnUiThread {
                consumption = auto.consumption
                tank = auto.tank
                val consText = "${auto.consumption} л./100 км"
                val tankText = "${auto.tank} л."
                with(binding){
                    FuelNoteAddAutoCons.text = consText
                    FuelNoteAddAutoTank.text = tankText
                }
                setTotal()
            }
        }
    }

    private fun addNote(){
        val journalApi = retrofit.create(JournalApi::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            val status = journalApi.addFuel(token = token,
                date = date.toString(),
                f_fuel = fF,
                f_odo = kmF,
                fuel_delta = fuelDelta,
                milleage = kmF-kmS)
            runOnUiThread {
                if (status.status == "ok"){
                    finish()
                    Toast.makeText(this@AddNoteFuelActivity,
                        "Запись успешно добавлена!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@AddNoteFuelActivity,
                        "Ошибка! Запись не добавлена", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}