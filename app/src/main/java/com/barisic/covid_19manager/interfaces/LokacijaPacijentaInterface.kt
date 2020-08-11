package com.barisic.covid_19manager.interfaces

import com.barisic.covid_19manager.models.LokacijaPacijenta
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface LokacijaPacijentaInterface {

    @POST("api/lokacije")
    fun postLokacijaPacijenta(
        @Header("Authorization") token: String,
        @Body lokacija: LokacijaPacijenta
    ): Call<Void>
}