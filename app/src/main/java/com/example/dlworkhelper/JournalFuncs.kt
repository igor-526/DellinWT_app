package com.example.dlworkhelper

import com.example.dlworkhelper.dataclasses.FuelNote

class JournalFuncs {
    fun getFuelInfo(notes: List<FuelNote>): Array<String> {
        var totalKM = 0
        var totalF = 0f
        for (note in notes) {
            totalKM += note.km
            totalF += note.fuel
        }
        return arrayOf(totalKM.toString(), totalF.toString())
    }
}