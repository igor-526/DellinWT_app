package com.example.dlworkhelper.mainApp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import com.example.dlworkhelper.R
import com.example.dlworkhelper.database.MainDB
import com.example.dlworkhelper.databinding.FragmentMenuJournalBinding
import com.example.dlworkhelper.databinding.FragmentMenuMainBinding
import com.example.dlworkhelper.dataclasses.Profile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FragmentMenuMain : Fragment() {

    companion object {
        private lateinit var binding: FragmentMenuMainBinding
        fun update(profile: Profile){
            with(binding){
                FragmentMainUsername.text = profile.username
                val hoursAllF = "${profile.hoursAll} ч."
                val hoursWorkedF = "${profile.hoursWorked} ч."
                FragmentMainHoursAll.text = hoursAllF
                FragmentMainHoursWorked.text = hoursWorkedF
                FragmentMainHoursProgress.max = profile.hoursAll
                FragmentMainHoursProgress.progress = profile.hoursWorked
                FragmentMainLastOdo.text = "Одометр: ${profile.lastKm} км"
                FragmentMainLastFuel.text = "Топливо: ${profile.lastFuel} л."
                FragmentMainDrivingFuel.text = "${profile.drivingF} л."
                FragmentMainDrivingKM.text = "${profile.drivingKM} км"
                FragmentMainSalaryAvance.text = "${profile.salaryAvance} р."
                FragmentMainSalaryZP.text = "${profile.salary} р."
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuMainBinding.inflate(layoutInflater)

        binding.FragmentMainMenuButton.setOnClickListener {
            (activity as MainActivity).openMenu()
        }

        CoroutineScope(Dispatchers.IO).launch {
            val DAO = MainActivity.db.getSettingsDAO()
            val profile = Profile(
                username = DAO.getSettingValue("username"),
                hoursWorked = DAO.getSettingValue("hours_worked").toInt(),
                hoursAll = DAO.getSettingValue("hours_all").toInt(),
                drivingKM = DAO.getSettingValue("totalKM").toInt(),
                drivingF = DAO.getSettingValue("totalF").toFloat(),
                salaryAvance = 0,
                salary = 0,
                lastFuel = DAO.getSettingValue("last_fuel").toFloat(),
                lastKm = DAO.getSettingValue("last_km").toInt()
            )
            activity?.runOnUiThread {
                update(profile)
            }
        }
        return binding.root
    }
}