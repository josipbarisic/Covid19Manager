package com.barisic.covid_19manager.repositories

import com.barisic.covid_19manager.interfaces.EpidemiologInterface
import com.barisic.covid_19manager.models.Epidemiolog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.util.*

class EpidemiologRepository(private val epidemiologService: EpidemiologInterface) {

    fun getAllEpidemiolozi(completion: (epidemiolozi: ArrayList<Epidemiolog>?) -> Unit) {
        epidemiologService.getAllEpidemiolozi().enqueue(object : Callback<ArrayList<Epidemiolog>> {
            override fun onResponse(
                call: Call<ArrayList<Epidemiolog>>,
                response: Response<ArrayList<Epidemiolog>>
            ) {
                completion(response.body())
                Timber.d("Number of epidemiologists -> ${response.body()?.size}")
            }

            override fun onFailure(call: Call<ArrayList<Epidemiolog>>, t: Throwable) {
                completion(null)
                Timber.d(t)
            }
        })
    }

}