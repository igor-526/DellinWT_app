package com.example.dlworkhelper.retrofit

import com.example.dlworkhelper.dataclasses.User
import retrofit2.http.GET
import retrofit2.http.Headers

interface UserApi {
    @Headers("Authorization: fshvdknvjk")
    @GET("user")
    suspend fun getUserInfo(): User
}