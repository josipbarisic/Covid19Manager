package com.barisic.covid_19manager.repositories

import com.barisic.covid_19manager.interfaces.PacijentInterface
import com.barisic.covid_19manager.models.Pacijent
import com.barisic.covid_19manager.util.ON_FAILURE
import com.barisic.covid_19manager.util.ON_RESPONSE
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class PacijentRepository(private val pacijentService: PacijentInterface) {

    fun getPacijent(oib: Long, pacijent: (response: Pacijent?, from: String) -> Unit) {
        pacijentService.getPacijent(oib).enqueue(object : Callback<Pacijent> {
            override fun onResponse(call: Call<Pacijent>, response: Response<Pacijent>) {
                pacijent(response.body(), ON_RESPONSE)
            }

            override fun onFailure(call: Call<Pacijent>, t: Throwable) {
                pacijent(null, ON_FAILURE)
            }
        })
    }

    fun updatePacijent(oib: Long, pacijent: Pacijent) {
        pacijentService.updatePacijent(oib, pacijent).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Timber.d("UPDATE onResponse: UPDATED -> ${response.code()}")
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Timber.d("UPDATE onFailure: FAILED -> ${t.message}")
            }
        })
    }
}