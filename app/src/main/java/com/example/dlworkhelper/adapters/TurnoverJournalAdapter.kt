package com.example.dlworkhelper.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dlworkhelper.R
import com.example.dlworkhelper.databinding.TurnovernoteItemBinding
import com.example.dlworkhelper.dataclasses.TurnoverNote

class TurnoverJournalAdapter(val listener: Listener) : ListAdapter<TurnoverNote, TurnoverJournalAdapter.Holder>(Comparator()){
    class Holder(view: View) : RecyclerView.ViewHolder(view){
        private val binding = TurnovernoteItemBinding.bind(view)
        fun bind(turnoverNote: TurnoverNote, listener: Listener) = with(binding){
            val cashText = turnoverNote.cash.toString() + " р."
            TurnoverNoteDate.text = turnoverNote.date.toString()
            TurnoverNoteCash.text = cashText
            TurnoverGoToNote.setOnClickListener {
                listener.onClickTurnover(turnoverNote)
            }
        }
    }

    class Comparator : DiffUtil.ItemCallback<TurnoverNote>() {
        override fun areItemsTheSame(oldItem: TurnoverNote, newItem: TurnoverNote): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TurnoverNote, newItem: TurnoverNote): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.turnovernote_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position), listener)
    }

    interface Listener{
        fun onClickTurnover(turnoverNote: TurnoverNote)
    }
}