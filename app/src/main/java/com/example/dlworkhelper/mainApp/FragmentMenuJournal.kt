package com.example.dlworkhelper.mainApp

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieDrawable
import com.example.dlworkhelper.notes.AddNoteFuelActivity
import com.example.dlworkhelper.notes.AddNoteTimeActivity
import com.example.dlworkhelper.notes.AddNoteTurnoverActivity
import com.example.dlworkhelper.R
import com.example.dlworkhelper.adapters.FuelJournalAdapter
import com.example.dlworkhelper.adapters.TimeJournalAdapter
import com.example.dlworkhelper.adapters.TurnoverJournalAdapter
import com.example.dlworkhelper.database.FuelNoteDB
import com.example.dlworkhelper.database.MainDB
import com.example.dlworkhelper.database.TimeNoteDB
import com.example.dlworkhelper.database.TurnoverNoteDB
import com.example.dlworkhelper.databinding.FragmentMenuJournalBinding
import com.example.dlworkhelper.databinding.ModalFuelBinding
import com.example.dlworkhelper.databinding.ModalTimeBinding
import com.example.dlworkhelper.databinding.ModalTurnoverBinding
import com.example.dlworkhelper.dataclasses.FuelNote
import com.example.dlworkhelper.dataclasses.FuelResponse
import com.example.dlworkhelper.dataclasses.TimeNote
import com.example.dlworkhelper.dataclasses.TimeResponse
import com.example.dlworkhelper.dataclasses.TurnoverNote
import com.example.dlworkhelper.dataclasses.TurnoverResponse
import com.example.dlworkhelper.retrofit.JournalApi
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.coroutines.coroutineContext


class FragmentMenuJournal : Fragment(), TimeJournalAdapter.Listener,
    FuelJournalAdapter.Listener, TurnoverJournalAdapter.Listener{
    private lateinit var timeNotes: List<TimeNoteDB>
    private lateinit var fuelNotes: List<FuelNoteDB>
    private lateinit var turnoverNotes: List<TurnoverNoteDB>
    private lateinit var binding: FragmentMenuJournalBinding

    companion object{
        lateinit var timeAdapter: TimeJournalAdapter
        lateinit var fuelAdapter: FuelJournalAdapter
        lateinit var turnoverAdapter: TurnoverJournalAdapter
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuJournalBinding.inflate(layoutInflater)
        with(binding){
            FragmentJournalLottie.repeatCount = 0
            FragmentJournalLottie.repeatMode = LottieDrawable.RESTART
            FragmentJournalLottie.playAnimation()
        }
        timeAdapter = TimeJournalAdapter(this@FragmentMenuJournal)
        fuelAdapter = FuelJournalAdapter(this@FragmentMenuJournal)
        turnoverAdapter = TurnoverJournalAdapter(this@FragmentMenuJournal)
        binding.journalRecycler.layoutManager = LinearLayoutManager(context)
        CoroutineScope(Dispatchers.IO).launch{
            timeNotes = MainActivity.db.getJournalDAO().getAllTime()
            fuelNotes = MainActivity.db.getJournalDAO().getAllFuel()
            turnoverNotes = MainActivity.db.getJournalDAO().getAllTurnover()
            activity?.runOnUiThread {
                timeAdapter.submitList(timeNotes)
                fuelAdapter.submitList(fuelNotes)
                turnoverAdapter.submitList(turnoverNotes)
                setTime()
            }
        }

        with(binding){
            journalTabs.visibility = View.VISIBLE
            journalRecycler.visibility = View.VISIBLE
            FragmentJournalLottieLayout.visibility = View.GONE
            FragmentJournalLottie.pauseAnimation()}
            binding.journalTabs.addOnTabSelectedListener(object : OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 0)
                    setTime()
                if (tab?.position == 1)
                    setFuel()
                if (tab?.position == 2)
                    setTurnover()
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
        binding.FragmentJournalButtonAdd.setOnClickListener {
            when(binding.journalTabs.selectedTabPosition){
                0 ->{
                    val intent = Intent(this.requireContext(), AddNoteTimeActivity::class.java)
                    startActivity(intent)
                }
                1 ->{
                    val intent = Intent(this.requireContext(), AddNoteFuelActivity::class.java)
                    startActivity(intent)
                }
                2 ->{
                    val intent = Intent(this.requireContext(), AddNoteTurnoverActivity::class.java)
                    startActivity(intent)
                }
                else -> {}
            }
        }
        return binding.root
    }

    override fun onClickTime(timeNote: TimeNoteDB, position: Int) {
        val timeNoteView = LayoutInflater.from(context)
            .inflate(R.layout.modal_time, null)
        val builder = AlertDialog.Builder(context).setView(timeNoteView)
        val binding = ModalTimeBinding.bind(timeNoteView)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val start = LocalDateTime.parse(timeNote.start, formatter)
        val end = LocalDateTime.parse(timeNote.end, formatter)
        val timeNoteItem = builder.show()
        with(binding){
            if (timeNote.c == 1)
                timeModalDayStatus.text = "Рабочий день"
            if (timeNote.c == 2)
                timeModalDayStatus.text = "Выходной день"
            val startText = "Начало: " + start.hour.toString().padStart(2, '0') +
                    ":" + start.minute.toString().padEnd(2, '0')
            val endText = "Окончание: " + end.hour.toString().padStart(2, '0') +
                    ":" + end.minute.toString().padEnd(2, '0')
            val totalText = "Засчитано: " + timeNote.total + " ч."
            timeModalStart.text = startText
            timeModalEnd.text = endText
            timeModalTotal.text = totalText
            timeModalClose.setOnClickListener {
                timeNoteItem.dismiss()
            }
            timeModalDelete.setOnClickListener{
                val confirm = AlertDialog.Builder(context)
                confirm.setTitle("Подтверждение")
                confirm.setMessage("Вы действительно хотите удалить запись?")
                confirm.setNegativeButton("Оставить"){
                    dialog, _ -> dialog.cancel()
                }
                confirm.setPositiveButton("Удалить"){
                    dialog, _ ->
                    run {
                        CoroutineScope(Dispatchers.IO).launch {
                            val status = MainActivity.journalAPI.deleteTime(MainActivity.token, timeNote.id)
                            activity?.runOnUiThread {
                                if (status.status == "OK") {
                                    dialog.cancel()
                                    timeNoteItem.dismiss()
                                    deleteTime(timeNote.id)
                                    Toast.makeText(context, "Запись успешно удалена!",
                                        Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
                confirm.show()
            }
            timeModalChange.setOnClickListener {
                val intent = Intent(context, AddNoteTimeActivity::class.java)
                intent.putExtra("id", timeNote.id)
                intent.putExtra("start", timeNote.start)
                intent.putExtra("end", timeNote.end)
                intent.putExtra("c", timeNote.c)
                intent.putExtra("total", timeNote.total)
                startActivity(intent)
                timeNoteItem.dismiss()
            }
        }
    }

    override fun onClickFuel(fuelNote: FuelNoteDB, position: Int) {
        val fuelNoteView = LayoutInflater.from(context)
            .inflate(R.layout.modal_fuel, null)
        val builder = AlertDialog.Builder(context).setView(fuelNoteView)
        val binding = ModalFuelBinding.bind(fuelNoteView)
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
                val intent = Intent(context, AddNoteFuelActivity::class.java)
                intent.putExtra("note_id", fuelNote.id)
                startActivity(intent)

            }
            fuelModalDelete.setOnClickListener {
                val confirm = AlertDialog.Builder(context)
                confirm.setTitle("Подтверждение")
                confirm.setMessage("Вы действительно хотите удалить запись?")
                confirm.setNegativeButton("Оставить"){
                        dialog, _ -> dialog.cancel()
                }
                confirm.setPositiveButton("Удалить"){
                        dialog, _ ->
                    run {
                        CoroutineScope(Dispatchers.IO).launch {
                            val status = MainActivity.journalAPI.deleteFuel(MainActivity.token, fuelNote.id)
                            activity?.runOnUiThread {
                                if (status.status == "OK") {
                                    dialog.cancel()
                                    fuelNoteItem.dismiss()



                                    Toast.makeText(context, "Запись успешно удалена!",
                                        Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
                confirm.show()
            }
        }
    }

    override fun onClickTurnover(turnoverNote: TurnoverNoteDB, position: Int) {
        val turnoverNoteView = LayoutInflater.from(context)
            .inflate(R.layout.modal_turnover, null)
        val builder = AlertDialog.Builder(context).setView(turnoverNoteView)
        val binding = ModalTurnoverBinding.bind(turnoverNoteView)
        val turnoverNoteItem = builder.show()
        with(binding){
            val cText = "Заработано: " + turnoverNote.cash.toString() + " р."
            turnoverModalCash.text = cText
            turnoverModalClose.setOnClickListener {
                turnoverNoteItem.dismiss()
            }
            turnoverModalChange.setOnClickListener{
                val intent = Intent(context, AddNoteTurnoverActivity::class.java)
                intent.putExtra("id", turnoverNote.id)
                intent.putExtra("cash", turnoverNote.cash)
                intent.putExtra("date", turnoverNote.date)
                startActivity(intent)
            }
            turnoverModalDelete.setOnClickListener {
                val confirm = AlertDialog.Builder(context)
                confirm.setTitle("Подтверждение")
                confirm.setMessage("Вы действительно хотите удалить запись?")
                confirm.setNegativeButton("Оставить"){
                        dialog, _ -> dialog.cancel()
                }
                confirm.setPositiveButton("Удалить"){
                        dialog, _ ->
                    run {
                        CoroutineScope(Dispatchers.IO).launch {
                            val status = MainActivity.journalAPI.deleteTurnover(MainActivity.token, turnoverNote.id)
                            activity?.runOnUiThread {
                                if (status.status == "OK") {
                                    dialog.cancel()
                                    turnoverNoteItem.dismiss()


                                    val currentList = turnoverAdapter.currentList.toMutableList()
                                    currentList.removeAt(position)
                                    turnoverAdapter.submitList(currentList)


                                    Toast.makeText(context, "Запись успешно удалена!",
                                        Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                    }
                confirm.show()
            }
        }
    }

    private fun setTime(){

        with(binding){
            journalRecycler.adapter = timeAdapter
            val status1VText = " ч."
            val status2VText = " ч."
            journalStatus1Title.text = "Отработано: "
            journalStatus1Value.text = status1VText
            journalStatus2Title.text = "До нормы: "
            journalStatus2Value.text = status2VText
        }
    }

    private fun setFuel(){
        with(binding){
            journalRecycler.adapter = fuelAdapter
//            val status1VText = fuelNotes.total_km.toString() + " км"
//            val status2VText = fuelNotes.total_fuel.toString() + " л."
//            journalStatus1Title.text = "Проехали: "
//            journalStatus1Value.text = status1VText
//            journalStatus2Title.text = "Сожгли: "
//            journalStatus2Value.text = status2VText
        }
    }

    private fun setTurnover(){
        with(binding){
            journalRecycler.adapter = turnoverAdapter
//            val status1VText = turnoverNotes.total.toString() + " р."
//            journalStatus1Title.text = "Итого: "
//            journalStatus1Value.text = status1VText
//            journalStatus2Title.text = "Премия: "
//            journalStatus2Value.text = "null"
        }
    }

    private fun deleteTime(id: Int){
        CoroutineScope(Dispatchers.IO).launch {
            MainActivity.db.getJournalDAO().deleteTime(id)
            timeNotes = MainActivity.db.getJournalDAO().getAllTime()
            activity?.runOnUiThread {
                timeAdapter.submitList(timeNotes)
            }
        }
    }
}