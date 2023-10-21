package com.example.dlworkhelper.mainApp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.example.dlworkhelper.R
import com.example.dlworkhelper.retrofit.UserApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FragmentMenuMain : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_menu_main, container, false)
        val userName: TextView = view.findViewById(R.id.username)
        val hoursAll: TextView = view.findViewById(R.id.hours_all)
        val hoursWorked: TextView = view.findViewById(R.id.hours_worked)
        val hoursProgress: ProgressBar = view.findViewById(R.id.hours_progress)
        val retrofit = Retrofit.Builder().baseUrl("http://80.87.192.255:5000")
            .addConverterFactory(GsonConverterFactory.create()).build()
        val userAPI = retrofit.create(UserApi::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            val user = userAPI.getUserInfo()
            activity?.runOnUiThread {
                userName.text = user.username
                val hoursAllF = user.hours_all.toString() + " ч."
                val hoursWorkedF = user.hours_worked.toString() + " ч."
                hoursAll.text = hoursAllF
                hoursWorked.text = hoursWorkedF
                hoursProgress.max = user.hours_all
                hoursProgress.progress = user.hours_worked
            }
        }
        return view
    }
}