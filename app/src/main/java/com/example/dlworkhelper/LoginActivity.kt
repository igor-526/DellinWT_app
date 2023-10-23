package com.example.dlworkhelper

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.dlworkhelper.database.MainDB
import com.example.dlworkhelper.database.SettingsDB
import com.example.dlworkhelper.mainApp.MainActivity
import com.example.dlworkhelper.retrofit.UserApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val tokenfield: EditText = findViewById(R.id.loginEditText)
        val loginbutton: Button = findViewById(R.id.loginIdButton)
        val loginCodeButton: Button = findViewById(R.id.loginCodeButton)
        val logintext: TextView = findViewById(R.id.loginText)
        val db = MainDB.getDB(this)
        val retrofit = Retrofit.Builder().baseUrl("http://80.87.192.255:5000")
            .addConverterFactory(GsonConverterFactory.create()).build()
        val userAPI = retrofit.create(UserApi::class.java)
        var userID = ""
        loginbutton.setOnClickListener{
            CoroutineScope(Dispatchers.IO).launch {
                    val status = userAPI.getCode(tokenfield.text.toString())
                runOnUiThread {
                    when (status.status){
                        "no_header" -> {
                            logintext.text = "Поле ID не может быть пустым!"
                            logintext.setTextColor(Color.parseColor("#FF0000"))
                        }
                        "error" -> {
                            logintext.text = "Ошибка при вводе!"
                            logintext.setTextColor(Color.parseColor("#FF0000"))
                        }
                        "not found" -> {
                            logintext.text = "Пользователь с таким ID не найден!"
                            logintext.setTextColor(Color.parseColor("#FF0000"))
                        }
                        else -> {
                            logintext.text = "Здравствуйте, ${status.status}!\n" +
                                    "В Ваш Telegram отправлен код подтвержения\n" +
                                    "Пожалуйста, введите его ниже!"
                            logintext.setTextColor(Color.parseColor("#FFFFFF"))
                            userID = tokenfield.text.toString()
                            tokenfield.text = null
                            loginbutton.visibility = View.GONE
                            loginCodeButton.visibility = View.VISIBLE
                        }
                    }

                    }
                }
            }
        loginCodeButton.setOnClickListener{
            CoroutineScope(Dispatchers.IO).launch {
                val status = userAPI.getToken(userID, tokenfield.text.toString())
                runOnUiThread {
                    when (status.status){
                        "missmatching" -> {
                            logintext.text = "Введён неверный код!"
                            logintext.setTextColor(Color.parseColor("#FF0000"))
                        }
                        "error" -> {
                            logintext.text = "Введён неверный код!"
                            logintext.setTextColor(Color.parseColor("#FF0000"))
                        }
                        else -> {
                            CoroutineScope(Dispatchers.IO).launch {
                                db.getSettingsDAO()
                                    .insertSetting(SettingsDB("auth_token", status.status))
                                runOnUiThread {
                                    intent = Intent(this@LoginActivity, StartActivity::class.java)
                                    startActivity(intent)
                                }
                            }
                        }
                    }
                }
            }
        }
        }
    }