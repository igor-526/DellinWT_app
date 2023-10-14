package com.example.dlworkhelper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dlworkhelper.databinding.TimenoteItemBinding

class TimeNoteAdapter: RecyclerView.Adapter<TimeNoteAdapter.TimeNoteHolder>() {
    val timenotelist = ArrayList<TimeNote>()
    class TimeNoteHolder(item: View): RecyclerView.ViewHolder(item) {
        val binding = TimenoteItemBinding.bind(item)
        fun bind(timeNote: TimeNote) = with(binding){
            val totalText = timeNote.total.toString() + " ч."
            TimeNoteDate.text = timeNote.date
            TimeNoteTime.text = timeNote.time
            TimeNoteTotal.text = totalText
            if (timeNote.dayOff)
                TimeNoteDayStatus.visibility = View.VISIBLE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeNoteHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.timenote_item, parent, false)
        return TimeNoteHolder(view)
    }

    override fun getItemCount(): Int {
        return timenotelist.size
    }

    override fun onBindViewHolder(holder: TimeNoteHolder, position: Int) {
        holder.bind(timenotelist[position])
    }

    fun addTimeNote(timeNote: TimeNote){
        timenotelist.add(timeNote)
        notifyDataSetChanged()
    }


}