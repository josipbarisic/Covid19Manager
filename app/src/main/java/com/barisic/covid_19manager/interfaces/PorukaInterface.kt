package com.barisic.covid_19manager.interfaces

import com.barisic.covid_19manager.models.Poruka
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface PorukaInterface {
    @POST
    fun sendMessage(@Header("Authorization") token: String, @Body poruka: Poruka)
}