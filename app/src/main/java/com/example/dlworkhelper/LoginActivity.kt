package com.example.dlworkhelper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.dlworkhelper.database.MainDB
import com.example.dlworkhelper.database.SettingsDB
import com.example.dlworkhelper.mainApp.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val tokenfield: EditText = findViewById(R.id.loginEditText)
        val loginbutton: Button = findViewById(R.id.loginButton)
        val db = MainDB.getDB(this)
        loginbutton.setOnClickListener{
            CoroutineScope(Dispatchers.IO).launch {
                db.getSettingsDAO().insertSetting(SettingsDB("auth_token", tokenfield.text.toString()))
                runOnUiThread {
                    val intent = Intent(this@LoginActivity, StartActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}