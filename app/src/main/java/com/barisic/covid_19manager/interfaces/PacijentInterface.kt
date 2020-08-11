package com.barisic.covid_19manager.interfaces

import com.barisic.covid_19manager.models.Pacijent
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*

interface PacijentInterface {

    @GET("api/pacijenti")
    suspend fun getAllPacijenti(@Header("Authorization") token: String): ArrayList<Pacijent>

    @GET("api/pacijenti/{OIB}")
    fun getPacijent(
        @Header("Authorization") token: String,
        @Path("OIB") oib: Long
    ): Call<JsonObject>

    @PUT("api/pacijenti/{OIB}")
    fun updatePacijent(/*@Header("Authorization") token: String,*/@Path("OIB") oib: Long,
                                                                  @Body pacijent: Pacijent
    ): Call<Void>
}