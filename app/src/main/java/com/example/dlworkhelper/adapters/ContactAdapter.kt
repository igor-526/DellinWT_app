package com.example.dlworkhelper.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dlworkhelper.R
import com.example.dlworkhelper.databinding.ItemContactBinding
import com.example.dlworkhelper.dataclasses.Contact

class ContactAdapter(private val listener: Listener): RecyclerView.Adapter<ContactAdapter.ContactHolder>(){
    val contactList = ArrayList<Contact>()
    class ContactHolder(item: View): RecyclerView.ViewHolder(item){
        val bindind = ItemContactBinding.bind(item)
        fun bind(contact: Contact, listener: Listener) = with(bindind){
            contactName.text = contact.name
            contactPosition.text = contact.position
            contactPhone.text = contact.phone
            goContact.setOnClickListener {
                listener.onClickContact(contact)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contact, parent, false)
        return ContactHolder(view)
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        holder.bind(contactList[position], listener)
    }

    fun addContact(contact: Contact){
        contactList.add(contact)
        notifyDataSetChanged()
    }

    fun deleteAll(){
        contactList.removeAll(contactList.toSet())
        notifyDataSetChanged()
    }

    interface Listener{
        fun onClickContact(contact: Contact)
    }

}