package com.example.dlworkhelper.retrofit

import com.example.dlworkhelper.dataclasses.ContactResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface ContactsApi {
    @GET("contacts")
    suspend fun getAllContacts(@Header("Authorization") token: String) : ContactResponse
}