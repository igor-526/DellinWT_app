package com.example.dlworkhelper.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dlworkhelper.R
import com.example.dlworkhelper.databinding.TimenoteItemBinding
import com.example.dlworkhelper.dataclasses.TimeNote

class TimeJournalAdapter(val listener: Listener) : ListAdapter<TimeNote, TimeJournalAdapter.Holder>(Comparator()) {
    class Holder(view: View) : RecyclerView.ViewHolder(view){
        private val binding = TimenoteItemBinding.bind(view)

        fun bind(timeNote: TimeNote, listener: Listener) = with(binding){
            val totalText = timeNote.total.toString() + " ч."
            TimeNoteDate.text = timeNote.date.toString()
            val timeF = timeNote.start + " - " + timeNote.end
            TimeNoteTime.text = timeF
            TimeNoteTotal.text = totalText
            if (timeNote.c == 2)
                TimeNoteDayStatus.visibility = View.VISIBLE
            TimeGoToNote.setOnClickListener {
                listener.onClickTime(timeNote)
            }
        }

    }

    class Comparator : DiffUtil.ItemCallback<TimeNote>(){
        override fun areItemsTheSame(oldItem: TimeNote, newItem: TimeNote): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TimeNote, newItem: TimeNote): Boolean {
            return oldItem == newItem
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.timenote_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position), listener)
    }

    interface Listener{
        fun onClickTime(timeNote: TimeNote)
    }
}