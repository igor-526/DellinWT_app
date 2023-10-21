package com.example.dlworkhelper.mainApp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import com.example.dlworkhelper.OnSwipeTouchListener
import com.example.dlworkhelper.R
import com.example.dlworkhelper.database.ContactDB
import com.example.dlworkhelper.database.MainDB
import com.example.dlworkhelper.database.SettingsDB
import com.example.dlworkhelper.retrofit.ContactsApi
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(){


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_placeholder, FragmentMenuMain()).commit()
        val bottomNav: BottomNavigationView = findViewById(R.id.navMenu)
        val mainPlaceholder: FrameLayout = findViewById(R.id.main_placeholder)
        val mainDrawer: DrawerLayout = findViewById(R.id.mainLayout)
        val mainMenu: NavigationView = findViewById(R.id.drawerMenu)
        bottomNav.setOnItemSelectedListener{
            when(it.itemId){
                R.id.menu_home_button -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_placeholder, FragmentMenuMain()).commit()
                }
                R.id.menu_journal_button -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_placeholder, FragmentMenuJournal()).commit()
                }
                R.id.menu_contacts_button -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_placeholder, FragmentMenuContacts()).commit()
                }
            }
            true
        }
        mainPlaceholder.setOnTouchListener(object : OnSwipeTouchListener(this@MainActivity){
            override fun onSwipeRight() {
                mainDrawer.openDrawer(mainMenu, true)
            }
        })
        sync()
    }

    fun sync(){
        val db = MainDB.getDB(this)
        val retrofit = Retrofit.Builder().baseUrl("http://80.87.192.255:5000")
            .addConverterFactory(GsonConverterFactory.create()).build()
        val contactsAPI = retrofit.create(ContactsApi::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            val contacts = contactsAPI.getAllContacts()
            db.getContactsDAO().deleteAllContacts()
            for (contact in contacts.items){
                val contDB = ContactDB(
                    id = null,
                    comment = contact.comment,
                    phone = contact.phone,
                    position = contact.position,
                    name = contact.name
                )
                db.getContactsDAO().insertContact(contDB)
            }
        }

    }
}