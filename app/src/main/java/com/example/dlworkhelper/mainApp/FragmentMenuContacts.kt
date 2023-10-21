package com.example.dlworkhelper.mainApp

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dlworkhelper.R
import com.example.dlworkhelper.adapters.ContactAdapter
import com.example.dlworkhelper.database.MainDB
import com.example.dlworkhelper.databinding.ContactModalBinding
import com.example.dlworkhelper.dataclasses.Contact
import com.example.dlworkhelper.retrofit.ContactsApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
class FragmentMenuContacts : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_menu_contacts, container, false)
        val rc: RecyclerView = v.findViewById(R.id.contact_recycler)
//        val retrofit = Retrofit.Builder().baseUrl("http://80.87.192.255:5000")
//            .addConverterFactory(GsonConverterFactory.create()).build()
//        val contactsAPI = retrofit.create(ContactsApi::class.java)
//        CoroutineScope(Dispatchers.IO).launch {
//            val cont = contactsAPI.getAllContacts()
//            activity?.runOnUiThread {
//                val adapter = ContactAdapter(this@FragmentMenuContacts)
//                adapter.submitList(cont.items)
//                rc.layoutManager = LinearLayoutManager(context)
//                rc.adapter = adapter
//
//            }
//        }


        val db = MainDB.getDB(this.requireContext())
        val adapter = ContactAdapter()
        rc.layoutManager = LinearLayoutManager(context)
        rc.adapter = adapter
        CoroutineScope(Dispatchers.IO).launch {
            val cc = db.getContactsDAO().getAllContacts()
            activity?.runOnUiThread {
                for (c in cc){
                    adapter.addContact(Contact(comment = c.comment,
                                                name = c.name,
                                                phone = c.phone,
                                                position = c.position))
                }
            }

        }



        return v
    }

//    override fun onClick(contact: Contact) {
//        val contactView = LayoutInflater.from(context).inflate(R.layout.contact_modal, null)
//        val builder = AlertDialog.Builder(context)
//            .setView(contactView)
//        val binding = ContactModalBinding.bind(contactView)
//        val contactItem = builder.show()
//        with(binding){
//            contactItemName.text = contact.name
//            contactItemComment.text = contact.comment
//            contactItemNumber.text = contact.phone
//            contactItemPosition.text = contact.position
//            contactItemCall.setOnClickListener {
//                val intent = Intent(Intent.ACTION_DIAL)
//                intent.data = Uri.parse("tel:" + contact.phone)
//                startActivity(intent)
//            }
//            contactItemWhatsapp.setOnClickListener {
//                val intent = Intent(Intent.ACTION_VIEW)
//                intent.data = Uri.parse("https://wa.me/" + contact.phone)
//                startActivity(intent)
//            }
//            contactItemBack.setOnClickListener {
//                contactItem.dismiss()
//            }
//        }
//
//
//    }

}
