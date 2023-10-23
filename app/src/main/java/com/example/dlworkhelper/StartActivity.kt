package com.example.dlworkhelper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.dlworkhelper.database.MainDB
import com.example.dlworkhelper.database.SettingsDB
import com.example.dlworkhelper.mainApp.MainActivity
import com.example.dlworkhelper.retrofit.UserApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        val db = MainDB.getDB(this)
        val retrofit = Retrofit.Builder().baseUrl("http://80.87.192.255:5000")
            .addConverterFactory(GsonConverterFactory.create()).build()
        CoroutineScope(Dispatchers.IO).launch {
            val token = db.getSettingsDAO().getSettingValue("auth_token")
            runOnUiThread {
                if (token == null) {
                    val intent = Intent(this@StartActivity, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    val userAPI = retrofit.create(UserApi::class.java)
                    CoroutineScope(Dispatchers.IO).launch {
                        val status = userAPI.checkToken(token)
                        runOnUiThread {
                            if (status.status == "True"){
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
                                    runOnUiThread {
                                        intent = Intent(this@StartActivity, MainActivity::class.java)
                                        startActivity(intent)
                                    }
                                }
                            } else {
                                Toast.makeText(
                                    this@StartActivity,
                                    "Токен недействителен",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent = Intent(this@StartActivity, LoginActivity::class.java)
                                startActivity(intent)
                            }
                        }
                    }
                }
            }
        }
    }
}