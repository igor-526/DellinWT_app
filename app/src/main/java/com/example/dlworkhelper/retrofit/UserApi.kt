package com.example.dlworkhelper.retrofit

import com.example.dlworkhelper.dataclasses.StatusResponse
import com.example.dlworkhelper.dataclasses.User
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface UserApi {
    @GET("user")
    suspend fun getUserInfo(@Header("Authorization") token: String): User

    @POST("auth")
    suspend fun getCode(@Header("user_id") id: String): StatusResponse

    @GET("auth/{id}")
    suspend fun getToken(@Path("id") id: String, @Header("code") code: String): StatusResponse

    @GET("auth/check")
    suspend fun checkToken(@Header("Authorization") token: String): StatusResponse
}