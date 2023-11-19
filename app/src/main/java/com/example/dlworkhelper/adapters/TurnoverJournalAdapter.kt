package com.example.dlworkhelper.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dlworkhelper.R
import com.example.dlworkhelper.database.TurnoverNoteDB
import com.example.dlworkhelper.databinding.ItemTurnovernoteBinding
import com.example.dlworkhelper.dataclasses.TurnoverNote
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TurnoverJournalAdapter(val listener: Listener) : ListAdapter<TurnoverNoteDB, TurnoverJournalAdapter.Holder>(Comparator()){
    class Holder(view: View) : RecyclerView.ViewHolder(view){
        private val binding = ItemTurnovernoteBinding.bind(view)
        fun bind(turnoverNote: TurnoverNoteDB, listener: Listener) = with(binding){
            val cashText = turnoverNote.cash.toString() + " р."
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val date = LocalDate.parse(turnoverNote.date, formatter)
            TurnoverNoteDate.text = date.dayOfMonth.toString().padStart(2, '0')
            TurnoverNoteCash.text = cashText
            TurnoverGoToNote.setOnClickListener {
                listener.onClickTurnover(turnoverNote, adapterPosition)
            }
        }
    }

    class Comparator : DiffUtil.ItemCallback<TurnoverNoteDB>() {
        override fun areItemsTheSame(oldItem: TurnoverNoteDB, newItem: TurnoverNoteDB): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TurnoverNoteDB, newItem: TurnoverNoteDB): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_turnovernote, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position), listener)
    }

    interface Listener{
        fun onClickTurnover(turnoverNote: TurnoverNoteDB, position: Int)
    }
}