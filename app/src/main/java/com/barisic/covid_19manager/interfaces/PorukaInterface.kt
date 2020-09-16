package com.barisic.covid_19manager.interfaces

import com.barisic.covid_19manager.models.Poruka
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface PorukaInterface {
    @POST("api/poruke/createMessage")
    fun sendMessage(@Body poruka: Poruka): Call<Void>
}