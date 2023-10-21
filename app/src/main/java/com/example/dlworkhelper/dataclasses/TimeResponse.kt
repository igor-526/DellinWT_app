package com.example.dlworkhelper.dataclasses

data class TimeResponse(
    val total: Float,
    val count: Int,
    val items: List<TimeNote>
)
