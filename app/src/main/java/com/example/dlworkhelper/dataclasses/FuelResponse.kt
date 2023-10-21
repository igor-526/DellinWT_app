package com.example.dlworkhelper.dataclasses

data class FuelResponse(val total_fuel: Float,
                        val total_km: Int,
                        val count: Int,
                        val items: List<FuelNote>)
