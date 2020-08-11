package com.barisic.covid_19manager.interfaces

import com.google.gson.JsonObject
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface JSONWebTokenInterface {
    @POST("b4015659-3179-4c26-b2e7-7219374ca2df/oauth2/v2.0/token/")
    fun getToken(@Body requestBody: RequestBody): Call<JsonObject>
}