package com.example.dlworkhelper.dataclasses

data class User(
    val base: String,
    val city: String,
    val hours_worked: Int,
    val hours_all: Int,
    val id: Int,
    val last_fuel: Float,
    val last_km: String,
    val mode: Int,
    val username: String
)
