package com.example.dlworkhelper

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dlworkhelper.retrofit.ContactsApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.ArrayList

class fragment_menu_contacts : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_menu_contacts, container, false)
        val contactlist: RecyclerView = v.findViewById(R.id.contact_recycler)
        val retrofit = Retrofit.Builder().baseUrl("http://192.168.0.55:5000")
            .addConverterFactory(GsonConverterFactory.create()).build()
        val contApi = retrofit.create(ContactsApi::class.java)
        val contacts = arrayListOf<Contact>()
        CoroutineScope(Dispatchers.IO).launch {
            val allcontsapi = contApi.getAllContacts()
            for (cont in allcontsapi){
                contacts.add(Contact(cont.id, cont.position, cont.name, cont.number))
            }
            (context as Activity).runOnUiThread {
                contactlist.layoutManager = LinearLayoutManager(context)
                contactlist.adapter = ContactsAdapter(contacts, context as Activity)
            }
        }
        return v
    }
}
