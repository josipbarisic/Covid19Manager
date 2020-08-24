package com.barisic.covid_19manager.repositories

import androidx.lifecycle.MutableLiveData
import com.barisic.covid_19manager.interfaces.PacijentInterface
import com.barisic.covid_19manager.models.Pacijent
import com.barisic.covid_19manager.util.PACIENT_ACCESS_FAILED
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class PacijentRepository(private val pacijentService: PacijentInterface) {

    fun getPacijent(token: String, oib: Long, pacijent: MutableLiveData<String>) {
        println("OIB FROM GETPACIJENT ----------------> $oib TOKEN FROM GETPACIJENT -----------------> {$token}")
        pacijentService.getPacijent(token, oib).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    pacijent.value = response.body().toString()
                    Timber.d(
                        "PACIJENT SUCCESSFUL RESPONSE ---------->${response.body()
                            .toString()}"
                    )
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Timber.d("PACIJENT FAILED -----------> MESSAGE: ${t.message}")
                pacijent.value = PACIENT_ACCESS_FAILED
            }
        })
    }

    //RADI BEZ AUTORIZACIJE (JSON tokena)
    fun updatePacijent(token: String, oib: Long, pacijent: Pacijent) {
        pacijentService.updatePacijent(token, oib, pacijent).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Timber.d("UPDATE onResponse: UPDATED -> ${response.code()}")
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Timber.d("UPDATE onFailure: FAILED -> ${t.message}")
            }
        })
    }
}