package com.example.dlworkhelper.retrofit

import com.example.dlworkhelper.dataclasses.ContactResponse
import retrofit2.http.GET
import retrofit2.http.Headers

interface ContactsApi {
    @Headers("Authorization: fshvdknvjk")
    @GET("contacts")
    suspend fun getAllContacts() : ContactResponse
}