package com.barisic.covid_19manager.repositories

import com.barisic.covid_19manager.interfaces.EpidemiologInterface
import com.barisic.covid_19manager.models.Epidemiolog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.util.*

class EpidemiologRepository(private val epidemiologService: EpidemiologInterface) {

    fun getAllEpidemiolozi(
        token: String,
        completion: (epidemiolozi: ArrayList<Epidemiolog>?) -> Unit
    ) {
        epidemiologService.getAllEpidemiolozi(token)
            .enqueue(object : Callback<ArrayList<Epidemiolog>> {
                override fun onFailure(call: Call<ArrayList<Epidemiolog>>, t: Throwable) {
                    completion(null)
                    Timber.d(t)
                }

                override fun onResponse(
                    call: Call<ArrayList<Epidemiolog>>,
                    response: Response<ArrayList<Epidemiolog>>
                ) {
                    response.body()!!.forEach {
                        Timber.d(it.zzjz)
                    }
                    completion(response.body())
                }
            })
    }

}