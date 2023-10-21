package com.example.dlworkhelper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.dlworkhelper.database.MainDB
import com.example.dlworkhelper.mainApp.MainActivity
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
                if (token == null){
                    val intent = Intent(this@StartActivity, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    if (checkToken(token)) {
                        intent = Intent(this@StartActivity, MainActivity::class.java)
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(this@StartActivity, "Токен недействителен", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@StartActivity, LoginActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    private fun checkToken(token: String) : Boolean{
        return token == "12345"
    }
}