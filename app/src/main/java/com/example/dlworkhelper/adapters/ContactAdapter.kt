package com.example.dlworkhelper.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dlworkhelper.R
import com.example.dlworkhelper.databinding.ContactItemBinding
import com.example.dlworkhelper.dataclasses.Contact

class ContactAdapter: RecyclerView.Adapter<ContactAdapter.ContactHolder>(){
    val contactList = ArrayList<Contact>()
    class ContactHolder(item: View): RecyclerView.ViewHolder(item){
        val bindind = ContactItemBinding.bind(item)
        fun bind(contact: Contact) = with(bindind){
            contactName.text = contact.name
            contactPosition.text = contact.position
            contactPhone.text = contact.phone
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.contact_item, parent, false)
        return ContactHolder(view)
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        holder.bind(contactList[position])
    }

    fun addContact(contact: Contact){
        contactList.add(contact)
        notifyDataSetChanged()
    }

}