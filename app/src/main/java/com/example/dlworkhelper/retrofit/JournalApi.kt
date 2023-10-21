package com.example.dlworkhelper.retrofit

import com.example.dlworkhelper.dataclasses.FuelResponse
import com.example.dlworkhelper.dataclasses.TimeResponse
import com.example.dlworkhelper.dataclasses.TurnoverResponse
import retrofit2.http.GET
import retrofit2.http.Headers

interface JournalApi {
    @Headers("Authorization: fshvdknvjk")
    @GET("journal/time/2023/10")
    suspend fun getTime(): TimeResponse

    @Headers("Authorization: fshvdknvjk")
    @GET("journal/fuel/2023/10")
    suspend fun getFuel(): FuelResponse

    @Headers("Authorization: fshvdknvjk")
    @GET("journal/turnover/2023/10")
    suspend fun getTurnover(): TurnoverResponse
}