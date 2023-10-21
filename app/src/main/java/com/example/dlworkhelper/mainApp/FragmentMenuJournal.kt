package com.example.dlworkhelper.mainApp

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dlworkhelper.R
import com.example.dlworkhelper.adapters.FuelJournalAdapter
import com.example.dlworkhelper.adapters.TimeJournalAdapter
import com.example.dlworkhelper.adapters.TurnoverJournalAdapter
import com.example.dlworkhelper.databinding.FuelModalBinding
import com.example.dlworkhelper.databinding.TimeModalBinding
import com.example.dlworkhelper.databinding.TurnoverModalBinding
import com.example.dlworkhelper.dataclasses.FuelNote
import com.example.dlworkhelper.dataclasses.TimeNote
import com.example.dlworkhelper.dataclasses.TurnoverNote
import com.example.dlworkhelper.retrofit.JournalApi
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class FragmentMenuJournal : Fragment(), TimeJournalAdapter.Listener,
    FuelJournalAdapter.Listener, TurnoverJournalAdapter.Listener {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_menu_journal, container, false)
        val rc: RecyclerView = v.findViewById(R.id.journal_recycler)
        val tabs: TabLayout = v.findViewById(R.id.journalTabs)
        val status1T: TextView = v.findViewById(R.id.journalStatus1Title)
        val status1V: TextView = v.findViewById(R.id.journalStatus1Value)
        val status2T: TextView = v.findViewById(R.id.journalStatus2Title)
        val status2V: TextView = v.findViewById(R.id.journalStatus2Value)
        val retrofit = Retrofit.Builder().baseUrl("http://80.87.192.255:5000")
            .addConverterFactory(GsonConverterFactory.create()).build()
        val journalAPI = retrofit.create(JournalApi::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            val timeNotes = journalAPI.getTime()
            activity?.runOnUiThread {
                val adapter = TimeJournalAdapter(this@FragmentMenuJournal)
                adapter.submitList(timeNotes.items)
                rc.layoutManager = LinearLayoutManager(context)
                rc.adapter = adapter
                status1T.text = "Отработано: "
                val status1VText = timeNotes.total.toInt().toString() + " ч."
                status1V.text = status1VText
                status2T.text = "До нормы: "
                val status2VText = timeNotes.total.toInt().toString() + " ч."
                status2V.text = status2VText
            }
        }
        tabs.addOnTabSelectedListener(object : OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 0){
                    CoroutineScope(Dispatchers.IO).launch {
                        val timeNotes = journalAPI.getTime()
                        activity?.runOnUiThread {
                            val adapter = TimeJournalAdapter(this@FragmentMenuJournal)
                            adapter.submitList(timeNotes.items)
                            rc.layoutManager = LinearLayoutManager(context)
                            rc.adapter = adapter
                            status1T.text = "Отработано: "
                            val status1VText = timeNotes.total.toInt().toString() + " ч."
                            status1V.text = status1VText
                            status2T.text = "До нормы: "
                            val status2VText = timeNotes.total.toInt().toString() + " ч."
                            status2V.text = status2VText
                        }
                    }
                }
                if (tab?.position == 1){
                    CoroutineScope(Dispatchers.IO).launch {
                        val fuelNotes = journalAPI.getFuel()
                        activity?.runOnUiThread {
                            val adapter = FuelJournalAdapter(this@FragmentMenuJournal)
                            adapter.submitList(fuelNotes.items)
                            rc.layoutManager = LinearLayoutManager(context)
                            rc.adapter = adapter
                            status1T.text = "Проехали: "
                            status2T.text = "Сожгли: "
                            val status1VText = fuelNotes.total_km.toString() + " км"
                            val status2VText = fuelNotes.total_fuel.toString() + " л."
                            status1V.text = status1VText
                            status2V.text = status2VText
                        }
                    }
                }
                if (tab?.position == 2){
                    CoroutineScope(Dispatchers.IO).launch {
                        val turnoverNotes = journalAPI.getTurnover()
                        activity?.runOnUiThread {
                            val adapter = TurnoverJournalAdapter(this@FragmentMenuJournal)
                            adapter.submitList(turnoverNotes.items)
                            rc.layoutManager = LinearLayoutManager(context)
                            rc.adapter = adapter
                            status1T.text = "Итого: "
                            status2T.text = "Премия: "
                            val status1VText = turnoverNotes.total.toString() + " р."
                            status1V.text = status1VText
                            status2V.text = "null"
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        return v
    }

    override fun onClickTime(timeNote: TimeNote) {
        val timeNoteView = LayoutInflater.from(context)
            .inflate(R.layout.time_modal, null)
        val builder = AlertDialog.Builder(context).setView(timeNoteView)
        val binding = TimeModalBinding.bind(timeNoteView)
        val timeNoteItem = builder.show()
        with(binding){
            if (timeNote.c == 1)
                timeModalDayStatus.text = "Рабочий день"
            if (timeNote.c == 2)
                timeModalDayStatus.text = "Выходной день"
            val startText = "Начало: " + timeNote.start
            val endText = "Окончание: " + timeNote.end
            val totalText = "Засчитано: " + timeNote.total + " ч."
            timeModalStart.text = startText
            timeModalEnd.text = endText
            timeModalTotal.text = totalText
            timeModalClose.setOnClickListener {
                timeNoteItem.dismiss()
            }
            timeModalDelete.setOnClickListener{
                Toast.makeText(context, "Функция пока не доступна :(", Toast.LENGTH_SHORT)
                    .show()
            }
            timeModalChange.setOnClickListener {
                Toast.makeText(context, "Функция пока не доступна :(", Toast.LENGTH_SHORT)
                    .show()
            }

        }
    }

    override fun onClickFuel(fuelNote: FuelNote) {
        val fuelNoteView = LayoutInflater.from(context)
            .inflate(R.layout.fuel_modal, null)
        val builder = AlertDialog.Builder(context).setView(fuelNoteView)
        val binding = FuelModalBinding.bind(fuelNoteView)
        val fuelNoteItem = builder.show()
        with(binding){
            val kmText = "Проехали: " + fuelNote.km.toString() + " км"
            val lText = "Сожгли: " + fuelNote.fuel.toString() + " л."
            fuelModalKM.text = kmText
            fuelModalL.text = lText
            fuelModalClose.setOnClickListener {
                fuelNoteItem.dismiss()
            }
            fuelModalChange.setOnClickListener {
                Toast.makeText(context, "Функция пока не доступна :(", Toast.LENGTH_SHORT)
                    .show()
            }
            fuelModalDelete.setOnClickListener {
                Toast.makeText(context, "Функция пока не доступна :(", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onClickTurnover(turnoverNote: TurnoverNote) {
        val turnoverNoteView = LayoutInflater.from(context)
            .inflate(R.layout.turnover_modal, null)
        val builder = AlertDialog.Builder(context).setView(turnoverNoteView)
        val binding = TurnoverModalBinding.bind(turnoverNoteView)
        val turnoverNoteItem = builder.show()
        with(binding){
            val cText = "Заработано: " + turnoverNote.cash.toString() + " р."
            turnoverModalCash.text = cText
            turnoverModalClose.setOnClickListener {
                turnoverNoteItem.dismiss()
            }
            turnoverModalChange.setOnClickListener{
                Toast.makeText(context, "Функция пока не доступна :(", Toast.LENGTH_SHORT)
                    .show()
            }
            turnoverModalDelete.setOnClickListener {
                Toast.makeText(context, "Функция пока не доступна :(", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}