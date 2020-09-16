package com.barisic.covid_19manager.repositories

import com.barisic.covid_19manager.interfaces.LokacijaPacijentaInterface
import com.barisic.covid_19manager.models.LokacijaPacijenta
import com.barisic.covid_19manager.util.LOCATION_SERVICE_TAG
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class LokacijaPacijentaRepository(private val lokacijaPacijentaService: LokacijaPacijentaInterface) {
    fun sendLokacija(lokacija: LokacijaPacijenta) {
        lokacijaPacijentaService.sendLokacijaPacijenta(lokacija).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Timber.tag(LOCATION_SERVICE_TAG).d("onResponse: ${response.code()}")
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Timber.tag(LOCATION_SERVICE_TAG).d("onFailure: ${t.message}")
            }
        })
    }
}