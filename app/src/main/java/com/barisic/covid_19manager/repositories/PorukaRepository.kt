package com.barisic.covid_19manager.repositories

import com.barisic.covid_19manager.interfaces.PorukaInterface
import com.barisic.covid_19manager.models.Poruka
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class PorukaRepository(private val porukaService: PorukaInterface) {
    fun sendMessage(token: String, poruka: Poruka, completion: (responseCode: Int?) -> Unit) {
        porukaService.sendMessage(token, poruka).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Timber.d(response.code().toString())
                completion(response.code())
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Timber.d(t)
                completion(null)
            }
        })
    }
}