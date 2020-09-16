package com.barisic.covid_19manager.interfaces

import com.barisic.covid_19manager.models.StanjePacijenta
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.*

interface StanjePacijentaInterface {
    @POST("api/stanja/createStanje")
    fun sendStanjePacijenta(@Body stanje: StanjePacijenta): Call<Void>

    @GET("api/stanja/GetStanjaByID/{ID}")
    fun getStanjaById(@Path("ID") id: String): Call<ArrayList<StanjePacijenta>>
}