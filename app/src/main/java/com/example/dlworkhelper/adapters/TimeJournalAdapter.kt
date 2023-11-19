package com.example.dlworkhelper.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dlworkhelper.R
import com.example.dlworkhelper.database.TimeNoteDB
import com.example.dlworkhelper.databinding.ItemTimenoteBinding
import com.example.dlworkhelper.dataclasses.TimeNote
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TimeJournalAdapter(private val listener: Listener) : ListAdapter<TimeNoteDB, TimeJournalAdapter.Holder>(Comparator()) {
    class Holder(view: View) : RecyclerView.ViewHolder(view){
        private val binding = ItemTimenoteBinding.bind(view)
        fun bind(timeNote: TimeNoteDB, listener: Listener) = with(binding){
            val totalText = timeNote.total.toString() + " ч."
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val start = LocalDateTime.parse(timeNote.start, formatter)
            val end = LocalDateTime.parse(timeNote.end, formatter)
            TimeNoteDate.text = start.dayOfMonth.toString().padStart(2, '0')
            val timeF = start.hour.toString().padStart(2, '0') + ":" +
                    start.minute.toString().padStart(2, '0') + " - " +
                    end.hour.toString().padStart(2, '0') + ":" +
                    end.minute.toString().padStart(2, '0')
            TimeNoteTime.text = timeF
            TimeNoteTotal.text = totalText
            if (timeNote.c == 2)
                TimeNoteDayStatus.visibility = View.VISIBLE
            TimeGoToNote.setOnClickListener {
                listener.onClickTime(timeNote, adapterPosition)
            }
        }

    }

    class Comparator : DiffUtil.ItemCallback<TimeNoteDB>(){
        override fun areItemsTheSame(oldItem: TimeNoteDB, newItem: TimeNoteDB): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TimeNoteDB, newItem: TimeNoteDB): Boolean {
            return oldItem == newItem
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_timenote, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position), listener)
    }

    interface Listener{
        fun onClickTime(timeNote: TimeNoteDB, position: Int)
    }
}