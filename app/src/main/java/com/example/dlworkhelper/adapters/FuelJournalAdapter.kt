package com.example.dlworkhelper.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dlworkhelper.R
import com.example.dlworkhelper.databinding.FuelnoteItemBinding
import com.example.dlworkhelper.dataclasses.FuelNote

class FuelJournalAdapter(val listener: Listener) : ListAdapter<FuelNote, FuelJournalAdapter.Holder>(Comparator()){
    class Holder(view: View) : RecyclerView.ViewHolder(view){
        private val binding = FuelnoteItemBinding.bind(view)
        fun bind(fuelNote: FuelNote, listener: Listener) = with(binding){
            val kmText = fuelNote.km.toString() + " км"
            val lText = fuelNote.fuel.toString() + " л."
            FuelNoteDate.text = fuelNote.date.toString()
            FuelNoteKm.text = kmText
            FuelNoteL.text = lText
            FuelGoToNote.setOnClickListener {
                listener.onClickFuel(fuelNote)
            }
        }

    }

    class Comparator : DiffUtil.ItemCallback<FuelNote>() {
        override fun areItemsTheSame(oldItem: FuelNote, newItem: FuelNote): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FuelNote, newItem: FuelNote): Boolean {
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
        fun onClickFuel(fuelNote: FuelNote)
    }
}