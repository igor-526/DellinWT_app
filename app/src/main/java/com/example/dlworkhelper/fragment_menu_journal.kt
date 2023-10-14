package com.example.dlworkhelper

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dlworkhelper.databinding.FragmentMenuJournalBinding


class fragment_menu_journal : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_menu_journal, container, false)
        val rc: RecyclerView = v.findViewById(R.id.journal_recycler)
        val adapter = TimeNoteAdapter()
        rc.layoutManager = LinearLayoutManager(this.requireContext())
        rc.adapter = adapter
        adapter.addTimeNote(TimeNote("1", "07:30 - 19:20", 10.83f, false))
        adapter.addTimeNote(TimeNote("2", "07:30 - 19:20", 10.83f, false))
        adapter.addTimeNote(TimeNote("3", "07:30 - 19:20", 10.83f, false))
        adapter.addTimeNote(TimeNote("4", "07:30 - 19:20", 10.83f, false))
        adapter.addTimeNote(TimeNote("5", "07:30 - 19:20", 10.83f, false))
        adapter.addTimeNote(TimeNote("6", "07:30 - 19:20", 10.83f, false))
        adapter.addTimeNote(TimeNote("8", "07:30 - 19:20", 21.66f, true))
        adapter.addTimeNote(TimeNote("9", "07:30 - 19:20", 10.83f, false))
        adapter.addTimeNote(TimeNote("10", "07:30 - 19:20", 10.83f, false))
        adapter.addTimeNote(TimeNote("11", "07:30 - 19:20", 10.83f, false))
        adapter.addTimeNote(TimeNote("12", "07:30 - 19:20", 10.83f, false))
        adapter.addTimeNote(TimeNote("13", "07:30 - 19:20", 10.83f, false))
        adapter.addTimeNote(TimeNote("15", "07:30 - 19:20", 21.66f, true))
        adapter.addTimeNote(TimeNote("16", "07:30 - 19:20", 10.83f, false))
        adapter.addTimeNote(TimeNote("17", "07:30 - 19:20", 10.83f, false))
        adapter.addTimeNote(TimeNote("18", "07:30 - 19:20", 10.83f, false))
        adapter.addTimeNote(TimeNote("19", "07:30 - 19:20", 10.83f, false))
        adapter.addTimeNote(TimeNote("22", "07:30 - 19:20", 10.83f, false))
        adapter.addTimeNote(TimeNote("23", "07:30 - 19:20", 10.83f, false))
        adapter.addTimeNote(TimeNote("24", "07:30 - 19:20", 10.83f, false))
        adapter.addTimeNote(TimeNote("25", "07:30 - 19:20", 10.83f, false))
        adapter.addTimeNote(TimeNote("26", "07:30 - 19:20", 10.83f, false))
        adapter.addTimeNote(TimeNote("27", "07:30 - 19:20", 10.83f, false))
        adapter.addTimeNote(TimeNote("28", "07:30 - 19:20", 21.66f, true))
        adapter.addTimeNote(TimeNote("30", "07:30 - 19:20", 10.83f, false))
        adapter.addTimeNote(TimeNote("31", "07:30 - 19:20", 10.83f, false))




        return v
    }
}