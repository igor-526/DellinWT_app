package com.example.dlworkhelper.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dlworkhelper.R
import com.example.dlworkhelper.database.FuelNoteDB
import com.example.dlworkhelper.databinding.FuelnoteItemBinding
import com.example.dlworkhelper.dataclasses.FuelNote
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class FuelJournalAdapter(val listener: Listener) : ListAdapter<FuelNoteDB, FuelJournalAdapter.Holder>(Comparator()){
    class Holder(view: View) : RecyclerView.ViewHolder(view){
        private val binding = FuelnoteItemBinding.bind(view)
        fun bind(fuelNote: FuelNoteDB, listener: Listener) = with(binding){
            val kmText = fuelNote.km.toString() + " км"
            val lText = fuelNote.fuel.toString() + " л."
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val date = LocalDate.parse(fuelNote.date, formatter)
            FuelNoteDate.text = date.dayOfMonth.toString().padStart(2, '0')
            FuelNoteKm.text = kmText
            FuelNoteL.text = lText
            FuelGoToNote.setOnClickListener {
                listener.onClickFuel(fuelNote, adapterPosition)
            }
        }

    }

    class Comparator : DiffUtil.ItemCallback<FuelNoteDB>() {
        override fun areItemsTheSame(oldItem: FuelNoteDB, newItem: FuelNoteDB): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FuelNoteDB, newItem: FuelNoteDB): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fuelnote_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position), listener)
    }

    interface Listener{
        fun onClickFuel(fuelNote: FuelNoteDB, position: Int)
    }
}