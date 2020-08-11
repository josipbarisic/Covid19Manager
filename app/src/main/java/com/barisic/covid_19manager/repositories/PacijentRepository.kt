package com.barisic.covid_19manager.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.barisic.covid_19manager.interfaces.PacijentInterface
import com.barisic.covid_19manager.models.Pacijent
import com.barisic.covid_19manager.util.PACIENT_ACCESS_FAILED
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PacijentRepository(private val pacijentService: PacijentInterface) {

    fun getPacijent(token: String, oib: Long, pacijent: MutableLiveData<String>) {
        println("OIB FROM GETPACIJENT ----------------> $oib TOKEN FROM GETPACIJENT -----------------> {$token}")
        pacijentService.getPacijent(token, oib).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    pacijent.value = response.body().toString()
                    println("PACIJENT SUCCESSFUL RESPONSE ---------->${response.body().toString()}")
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                println("PACIJENT FAILED -----------> MESSAGE: " + t.message)
                pacijent.value = PACIENT_ACCESS_FAILED
            }
        })
    }

    //RADI BEZ AUTORIZACIJE (JSON tokena)
    fun updatePacijent(oib: Long, pacijent: Pacijent) {
        pacijentService.updatePacijent(oib, pacijent).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Log.d("PACIJENT", "onResponse: UPDATED -> ${response.code()}")
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d("PACIJENT", "onFailure: FAILED -> ${t.message}")
            }
        })
    }
}