package com.example.dlworkhelper.retrofit
import retrofit2.http.GET

interface UserApi {
    @GET("users/1")
    suspend fun getUserInfo(): User
}