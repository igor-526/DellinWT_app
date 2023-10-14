package com.example.dlworkhelper

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ContactsAdapter(var contacts: List<Contact>, var context: Context) : RecyclerView.Adapter<ContactsAdapter.MyViewHolder>(){

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view){
        val position: TextView = view.findViewById(R.id.contact_position)
        val name: TextView = view.findViewById(R.id.contact_name)
        val phoneNumber: TextView = view.findViewById(R.id.contact_number)
        val callButton: ImageButton = view.findViewById(R.id.contact_call)
        val whatsappButton: ImageButton = view.findViewById(R.id.contact_whatsapp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.contact_in_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return contacts.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.position.text = contacts[position].position
        holder.name.text = contacts[position].name
        holder.phoneNumber.text = contacts[position].number
        holder.callButton.setOnClickListener {

            val intent = Intent()
            intent.action = Intent.ACTION_DIAL
            intent.data = Uri.parse("tel:" + contacts[position].number)
            context.startActivity(intent)

        }

        holder.whatsappButton.setOnClickListener {

            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.data = Uri.parse("https://wa.me/" + contacts[position].number + "/")
            context.startActivity(intent)

        }
    }

}