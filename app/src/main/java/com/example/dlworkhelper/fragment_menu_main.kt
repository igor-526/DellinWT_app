package com.example.dlworkhelper

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.example.dlworkhelper.retrofit.UserApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class fragment_menu_main : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_menu_main, container, false)
        val userName: TextView = view.findViewById(R.id.username)
        val hoursAll: TextView = view.findViewById(R.id.hours_all)
        val hoursWorked: TextView = view.findViewById(R.id.hours_worked)
        val hoursProgress: ProgressBar = view.findViewById(R.id.hours_progress)

        val retrofit = Retrofit.Builder().baseUrl("http://192.168.0.55:5000")
            .addConverterFactory(GsonConverterFactory.create()).build()
        val userInfo = retrofit.create(UserApi::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            val user = userInfo.getUserInfo()
            (context as Activity).runOnUiThread {
                val userHoursWorkedFormatted = user.hours_worked.toString() + " ч."
                val userHoursAllFormatted = user.hours_all.toString() + " ч."
                userName.text = user.name
                hoursAll.text = userHoursAllFormatted
                hoursWorked.text = userHoursWorkedFormatted
                hoursProgress.max = user.hours_all
                hoursProgress.setProgress(user.hours_worked, true)
            }
        }
        return view
    }
}