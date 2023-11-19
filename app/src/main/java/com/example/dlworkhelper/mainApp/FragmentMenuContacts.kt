package com.example.dlworkhelper.mainApp

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView.OnQueryTextListener
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dlworkhelper.R
import com.example.dlworkhelper.adapters.ContactAdapter
import com.example.dlworkhelper.database.ContactDB
import com.example.dlworkhelper.database.MainDB
import com.example.dlworkhelper.databinding.FragmentMenuContactsBinding
import com.example.dlworkhelper.databinding.ModalContactBinding
import com.example.dlworkhelper.dataclasses.Contact
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FragmentMenuContacts : Fragment(), ContactAdapter.Listener{
    private lateinit var contacts: List<ContactDB>
    private lateinit var binding: FragmentMenuContactsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuContactsBinding.inflate(layoutInflater)
        val positions = arrayOf("Должность").toMutableList()
        val db = MainDB.getDB(this.requireContext())
        val adapter = ContactAdapter(this@FragmentMenuContacts)
        CoroutineScope(Dispatchers.IO).launch {
            contacts = db.getContactsDAO().getAllContacts()
            activity?.runOnUiThread {
                for (contact in contacts){
                    adapter.addContact(Contact(comment = contact.comment,
                                                name = contact.name,
                                                phone = contact.phone,
                                                position = contact.position))
                    if (contact.position !in positions){positions.add(contact.position)}
                }
            }
        }
        val arrayAdapter = ArrayAdapter(this.requireContext(), android.R.layout.simple_spinner_dropdown_item, positions)
        with(binding){
            FragmentContactsRecycler.layoutManager = LinearLayoutManager(context)
            FragmentContactsRecycler.adapter = adapter
            FragmentContactsPositions.adapter = arrayAdapter
            FragmentContactsPositions.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (position == 0){
                        adapter.deleteAll()
                        CoroutineScope(Dispatchers.IO).launch {
                            contacts = db.getContactsDAO().getAllContacts()
                            activity?.runOnUiThread {
                                for (contact in contacts){
                                    adapter.addContact(Contact(comment = contact.comment,
                                        name = contact.name,
                                        phone = contact.phone,
                                        position = contact.position))
                                }
                            }
                        }
                    } else {
                        adapter.deleteAll()
                        CoroutineScope(Dispatchers.IO).launch {
                            contacts = db.getContactsDAO().getPositionContacts(positions[position])
                            activity?.runOnUiThread {
                                for (contact in contacts){
                                    adapter.addContact(Contact(comment = contact.comment,
                                        name = contact.name,
                                        phone = contact.phone,
                                        position = contact.position))
                                }
                            }
                        }


                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
            FragmentContactsSearch.setOnSearchClickListener {
                FragmentContactsPositions.visibility = View.GONE
            }
            FragmentContactsSearch.setOnCloseListener { onClose(FragmentContactsPositions) }
            FragmentContactsSearch.setOnSearchClickListener {
                FragmentContactsPositions.visibility = View.GONE
            }
            FragmentContactsSearch.setOnCloseListener { onClose(FragmentContactsPositions) }
            FragmentContactsSearch.setOnQueryTextListener(object : OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    adapter.deleteAll()
                    CoroutineScope(Dispatchers.IO).launch {
                        contacts = newText?.let { db.getContactsDAO().getSearchContacts(it) }!!
                        activity?.runOnUiThread {
                            for (contact in contacts){
                                adapter.addContact(Contact(comment = contact.comment,
                                    name = contact.name,
                                    phone = contact.phone,
                                    position = contact.position))
                            }
                        }
                    }
                    return true
                }
            })
        }
        return binding.root
    }

    private fun onClose(spinner: Spinner): Boolean {
        spinner.visibility = View.VISIBLE
        return false
    }

    override fun onClickContact(contact: Contact) {
        val contactView = LayoutInflater.from(context)
            .inflate(R.layout.modal_contact, null)
        val builder = AlertDialog.Builder(context).setView(contactView)
        val cbinding = ModalContactBinding.bind(contactView)
        val contactModal = builder.show()
        with(cbinding){
            contactItemName.text = contact.name
            contactItemPosition.text = contact.position
            contactItemComment.text = contact.comment
            contactItemNumber.text = contact.phone
            contactItemBack.setOnClickListener{contactModal.dismiss()}
            contactItemCall.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL,
                    Uri.parse("tel: ${contact.phone}"))
                startActivity(intent)
            }
            contactItemWhatsapp.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://wa.me/${contact.phone}"))
                startActivity(intent)
            }
        }
    }
}
