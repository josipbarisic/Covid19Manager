package com.barisic.covid_19manager.interfaces

import com.barisic.covid_19manager.models.Epidemiolog
import retrofit2.Call
import retrofit2.http.GET
import java.util.*

interface EpidemiologInterface {

    @GET("api/epidemiolozi")
    fun getAllEpidemiolozi(): Call<ArrayList<Epidemiolog>>

}