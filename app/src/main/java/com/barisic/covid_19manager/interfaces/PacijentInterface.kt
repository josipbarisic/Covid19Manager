package com.barisic.covid_19manager.interfaces

import com.barisic.covid_19manager.models.Pacijent
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface PacijentInterface {
    @GET("api/pacijenti/{OIB}")
    fun getPacijent(@Path("OIB") oib: Long): Call<Pacijent>

    @PUT("api/pacijenti/updatePacijent/{OIB}")
    fun updatePacijent(@Path("OIB") oib: Long, @Body pacijent: Pacijent): Call<Void>
}