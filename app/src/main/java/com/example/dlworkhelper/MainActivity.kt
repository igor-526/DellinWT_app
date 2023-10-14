package com.example.dlworkhelper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_placeholder, fragment_menu_main()).commit()

        val bottomNav: BottomNavigationView = findViewById(R.id.navMenu)
        bottomNav.setOnItemSelectedListener{
            when(it.itemId){
                R.id.menu_home_button -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_placeholder, fragment_menu_main()).commit()
                }
                R.id.menu_journal_button -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_placeholder, fragment_menu_journal()).commit()
                }
                R.id.menu_contacts_button -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_placeholder, fragment_menu_contacts()).commit()
                }
                R.id.menu_settings_button -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_placeholder, fragment_menu_settings()).commit()
                }
            }
            true
        }

    }
}