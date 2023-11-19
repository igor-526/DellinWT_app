package com.example.dlworkhelper.retrofit

import com.example.dlworkhelper.dataclasses.Auto
import com.example.dlworkhelper.dataclasses.FuelResponse
import com.example.dlworkhelper.dataclasses.StatusResponse
import com.example.dlworkhelper.dataclasses.TimeResponse
import com.example.dlworkhelper.dataclasses.TurnoverResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HEAD
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface JournalApi {

    @GET("journal/auto")
    suspend fun getAuto(@Header("Authorization") token: String): List<Auto>

    @GET("journal/time/2023/11")
    suspend fun getTime(@Header("Authorization") token: String): TimeResponse

    @GET("journal/fuel/2023/11")
    suspend fun getFuel(@Header("Authorization") token: String): FuelResponse

    @GET("journal/turnover/2023/11")
    suspend fun getTurnover(@Header("Authorization") token: String): TurnoverResponse

    @DELETE("journal/time/{id}")
    suspend fun deleteTime(@Header("Authorization") token: String, @Path("id") id: Int): StatusResponse

    @DELETE("journal/fuel/{id}")
    suspend fun deleteFuel(@Header("Authorization") token: String, @Path("id") id: Int): StatusResponse

    @DELETE("journal/turnover/{id}")
    suspend fun deleteTurnover(@Header("Authorization") token: String, @Path("id") id: Int): StatusResponse

    @POST("journal/fuel")
    suspend fun addFuel(@Header("Authorization") token: String,
                        @Header("milleage") milleage: Int,
                        @Header("fuel_delta") fuel_delta: Float,
                        @Header("f_odo") f_odo: Int,
                        @Header("f_fuel") f_fuel: Float,
                        @Header("date") date: String): StatusResponse

    @POST("journal/time")
    suspend fun addTime(@Header("Authorization") token: String,
                        @Header("start") start: String,
                        @Header("end") end: String,
                        @Header("c") c: Int,
                        @Header("total") total: Float): StatusResponse

    @PATCH("journal/time/{id}")
    suspend fun changeTime(@Header("Authorization") token: String,
                           @Header("start") start: String,
                           @Header("end") end: String,
                           @Header("c") c: Int,
                           @Header("total") total: Float,
                           @Path("id") id: Int): StatusResponse

    @POST("journal/turnover")
    suspend fun addTurnover(@Header("Authorization") token: String,
                            @Header("date") date: String,
                            @Header("cash") cash: Float): StatusResponse

    @PATCH("journal/turnover/{id}")
    suspend fun changeTurnover(@Header("Authorization") token: String,
                               @Header("date") date: String,
                               @Header("cash") cash: Float,
                               @Path("id") id: Int): StatusResponse
}