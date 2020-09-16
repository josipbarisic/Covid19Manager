package com.barisic.covid_19manager.repositories

import com.barisic.covid_19manager.interfaces.StanjePacijentaInterface
import com.barisic.covid_19manager.models.StanjePacijenta
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class StanjePacijentaRepository(private val stanjePacijentaService: StanjePacijentaInterface) {
    fun sendStanje(
        stanje: StanjePacijenta,
        responseCallback: (result: Boolean) -> Unit
    ) {
        Timber.tag("SEND_STANJE").d(stanje.toString())
        stanjePacijentaService.sendStanjePacijenta(stanje).enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Timber.tag("STANJE_RESPONSE").d("onFailure: ${t.message}")
                responseCallback(false)
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful)
                    responseCallback(true)
                else
                    responseCallback(false)
            }
        })
    }

    fun getStanjaPacijenta(
        id: String,
        responseCallback: (result: ArrayList<StanjePacijenta>?) -> Unit
    ) {
        stanjePacijentaService.getStanjaById(id)
            .enqueue(object : Callback<ArrayList<StanjePacijenta>> {
                override fun onResponse(
                    call: Call<ArrayList<StanjePacijenta>>,
                    response: Response<ArrayList<StanjePacijenta>>
                ) {
                    responseCallback(response.body())
                }

                override fun onFailure(call: Call<ArrayList<StanjePacijenta>>, t: Throwable) {
                    Timber.d("onFailure: FAILED -> ${t.message}")
                    responseCallback(null)
                }
            })
    }
}