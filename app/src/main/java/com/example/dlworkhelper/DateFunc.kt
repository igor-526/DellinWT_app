package com.example.dlworkhelper

import android.app.DatePickerDialog
import android.content.Context
import java.time.LocalDate
import java.util.Calendar

class DateFunc {
    fun dateToText(date: LocalDate): String{
        var monthString = ""
        when (date.monthValue) {
            1 -> monthString = "января"
            2 -> monthString = "февраля"
            3 -> monthString = "марта"
            4 -> monthString = "апреля"
            5 -> monthString = "мая"
            6 -> monthString = "июня"
            7 -> monthString = "июля"
            8 -> monthString = "августа"
            9 -> monthString = "сентября"
            10 -> monthString = "октября"
            11 -> monthString = "ноября"
            12 -> monthString = "декабря"
        }
        var dayString = ""
        when (date.dayOfWeek.value){
            1 -> dayString = "понедельник"
            2 -> dayString = "вторник"
            3 -> dayString = "среда"
            4 -> dayString = "четверг"
            5 -> dayString = "пятница"
            6 -> dayString = "суббота"
            7 -> dayString = "воскресенье"
        }
        return "${date.dayOfMonth} $monthString, $dayString"
    }

    fun getDatePickerDialog(context: Context): DatePickerDialog{
        val dpd = DatePickerDialog(context)
        val maxCalendar = Calendar.getInstance()
        val minCalendar = Calendar.getInstance()
        maxCalendar.set(LocalDate.now().year, LocalDate.now().monthValue-1, LocalDate.now().dayOfMonth)
        minCalendar.set(LocalDate.now().year, LocalDate.now().monthValue-2, 1)
        dpd.datePicker.maxDate = maxCalendar.timeInMillis
        dpd.datePicker.minDate = minCalendar.timeInMillis
        return dpd
    }
}