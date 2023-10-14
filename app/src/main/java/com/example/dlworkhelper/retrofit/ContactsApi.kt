package com.example.dlworkhelper.retrofit
import com.example.dlworkhelper.Contact
import retrofit2.http.GET

interface ContactsApi {
    @GET("contacts")
    suspend fun getAllContacts(): List<Contact>
}