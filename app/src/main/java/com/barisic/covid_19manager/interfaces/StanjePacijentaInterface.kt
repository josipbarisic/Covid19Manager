package com.barisic.covid_19manager.interfaces

import com.barisic.covid_19manager.models.StanjePacijenta
import retrofit2.Call
import retrofit2.http.*
import java.util.*

interface StanjePacijentaInterface {
    @POST("api/stanja/")
    fun postStanjePacijenta(
        @Header("Authorization") token: String,
        @Body stanje: StanjePacijenta
    ): Call<Void>

    @GET("api/stanja/GetStanjaByID/{ID}")
    fun getStanjaById(@Path("ID") oib: String): Call<ArrayList<StanjePacijenta>>
}