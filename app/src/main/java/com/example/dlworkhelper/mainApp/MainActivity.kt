package com.example.dlworkhelper.mainApp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentOnAttachListener
import com.example.dlworkhelper.BarcodeGeneratorActivity
import com.example.dlworkhelper.JournalFuncs
import com.example.dlworkhelper.notes.AddNoteFuelActivity
import com.example.dlworkhelper.OnSwipeTouchListener
import com.example.dlworkhelper.R
import com.example.dlworkhelper.adapters.TimeJournalAdapter
import com.example.dlworkhelper.database.AutoDB
import com.example.dlworkhelper.database.ContactDB
import com.example.dlworkhelper.database.FuelNoteDB
import com.example.dlworkhelper.database.MainDB
import com.example.dlworkhelper.database.SettingsDB
import com.example.dlworkhelper.database.TimeNoteDB
import com.example.dlworkhelper.database.TurnoverNoteDB
import com.example.dlworkhelper.databinding.ActivityMainBinding
import com.example.dlworkhelper.dataclasses.Profile
import com.example.dlworkhelper.retrofit.ContactsApi
import com.example.dlworkhelper.retrofit.JournalApi
import com.example.dlworkhelper.retrofit.UserApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(){
    lateinit var binding: ActivityMainBinding

    companion object{
        var token = ""
        var frag = "main"
        lateinit var db: MainDB
        lateinit var retrofit: Retrofit
        lateinit var journalAPI: JournalApi
        lateinit var contactsAPI: ContactsApi
        lateinit var userAPI: UserApi
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val fm = supportFragmentManager
        fm.beginTransaction()
            .replace(R.id.main_placeholder, FragmentMenuMain()).commit()
        db = MainDB.getDB(this@MainActivity)
        retrofit = Retrofit.Builder().baseUrl("http://80.87.192.255:5000")
            .addConverterFactory(GsonConverterFactory.create()).build()
        journalAPI = retrofit.create(JournalApi::class.java)
        contactsAPI = retrofit.create(ContactsApi::class.java)
        userAPI = retrofit.create(UserApi::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            token = db.getSettingsDAO().getSettingValue("auth_token")
            runOnUiThread {
                syncProfile()
                syncContacts()
            }
        }
        with (binding) {
            navMenu.setOnItemSelectedListener{
                when(it.itemId){
                    R.id.menu_home_button -> {
                        fm.beginTransaction()
                            .replace(R.id.main_placeholder, FragmentMenuMain(), "main").commit()
                        frag = "main"
                    }
                    R.id.menu_journal_button -> {
                        fm.beginTransaction()
                            .replace(R.id.main_placeholder, FragmentMenuJournal(), "journal").commit()
                        frag = "journal"
                    }
                    R.id.menu_contacts_button -> {
                        fm.beginTransaction()
                            .replace(R.id.main_placeholder, FragmentMenuContacts(), "contacts").commit()
                        frag = "contacts"
                    }
                }
                true
            }
            mainPlaceholder.setOnTouchListener(object : OnSwipeTouchListener(this@MainActivity){
                override fun onSwipeRight() {
                    mainLayout.openDrawer(drawerMenu, true)
                }
            })
            drawerMenu.setNavigationItemSelectedListener {
                when (it.itemId){
                    R.id.mainMenuDrawerCalculate -> {
                        val intent = Intent(this@MainActivity, AddNoteFuelActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    R.id.mainMenuDrawerGenerateBarcode -> {
                        val intent = Intent(this@MainActivity, BarcodeGeneratorActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    else -> {true}
                }
            }
        }
        setContentView(binding.root)
    }

    fun openMenu(){
        binding.mainLayout.openDrawer(binding.drawerMenu, true)
    }

    fun syncContacts(){
        CoroutineScope(Dispatchers.IO).launch {
            val contacts = contactsAPI.getAllContacts(token)
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
            val autos = journalAPI.getAuto(token)
            db.getAutoDAO().deleteAll()
            for (auto in autos){
                val autodb = AutoDB(id = auto.id,
                    name = auto.name,
                    consumption = auto.consumption,
                    tank = auto.tank)
                db.getAutoDAO().insertAuto(autodb)
            }
        }
    }

    fun syncProfile(){
        CoroutineScope(Dispatchers.IO).launch {
            val user = userAPI.getUserInfo(token)
            db.getSettingsDAO().insertSetting(SettingsDB("username", user.username))
            db.getSettingsDAO().insertSetting(SettingsDB("base", user.base))
            db.getSettingsDAO().insertSetting(SettingsDB("city", user.city))
            db.getSettingsDAO().insertSetting(SettingsDB("hours_worked", user.hours_worked.toString()))
            db.getSettingsDAO().insertSetting(SettingsDB("hours_all", user.hours_all.toString()))
            db.getSettingsDAO().insertSetting(SettingsDB("last_km", user.last_km))
            db.getSettingsDAO().insertSetting(SettingsDB("last_fuel", user.last_fuel.toString()))
            db.getSettingsDAO().insertSetting(SettingsDB("mode", user.mode.toString()))

            val timeNotes = journalAPI.getTime(token)
            val fuelNotes = journalAPI.getFuel(token)
            val turnoverNotes = journalAPI.getTurnover(token)
            val fuelInfo = JournalFuncs().getFuelInfo(fuelNotes.items)
            db.getSettingsDAO().insertSetting(SettingsDB("totalKM", fuelInfo[0]))
            db.getSettingsDAO().insertSetting(SettingsDB("totalF", fuelInfo[1]))

            runOnUiThread {
                if (frag == "main"){
                    val profile = Profile(
                        username = user.username,
                        hoursWorked = user.hours_worked,
                        hoursAll = user.hours_all,
                        drivingKM = fuelInfo[0].toInt(),
                        drivingF = fuelInfo[1].toFloat(),
                        salaryAvance = 0,
                        salary = 0,
                        lastFuel = user.last_fuel,
                        lastKm = user.last_km.toInt()
                    )
                    FragmentMenuMain.update(profile)
                }
            }

            db.getJournalDAO().deleteAllTime()
            db.getJournalDAO().deleteAllTurnover()
            db.getJournalDAO().deleteAllFuel()
            for (note in timeNotes.items){
                db.getJournalDAO().insertTimeNote(TimeNoteDB(
                        id = note.id,
                        start = note.start,
                        end = note.end,
                        total = note.total,
                        c = note.c)
                )
            }
            for (note in fuelNotes.items){
                db.getJournalDAO().insertFuelNote(FuelNoteDB(
                        id = note.id,
                        date = note.date,
                        fuel = note.fuel,
                        km = note.km
                    )
                )
            }
            for (note in turnoverNotes.items){
                db.getJournalDAO().insertTurnoverNote(TurnoverNoteDB(
                    id = note.id,
                    date = note.date,
                    cash = note.cash
                ))
            }
        }
    }

}