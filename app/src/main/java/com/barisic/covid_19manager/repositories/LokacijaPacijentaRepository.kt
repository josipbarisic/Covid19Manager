package com.barisic.covid_19manager.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.barisic.covid_19manager.interfaces.LokacijaPacijentaInterface
import com.barisic.covid_19manager.models.LokacijaPacijenta
import com.barisic.covid_19manager.util.LOCATION_SERVICE_TAG
import com.barisic.covid_19manager.util.TOKEN_ACCESS_FAILED
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LokacijaPacijentaRepository(
    private val jsonWebTokenRepository: JsonWebTokenRepository,
    private val lokacijaPacijentaService: LokacijaPacijentaInterface
) {
    private var jsonToken = MutableLiveData<String>()
    fun sendLokacija(lokacija: LokacijaPacijenta) {
        jsonWebTokenRepository.getJsonToken(jsonToken)
        if (!jsonToken.hasObservers()) {
            jsonToken.observeForever {
                when (it) {
                    TOKEN_ACCESS_FAILED -> {
                        println("TOKEN RESPONSE ----> NULL")
                    }
                    else -> {
                        lokacijaPacijentaService.postLokacijaPacijenta(it, lokacija)
                            .enqueue(object : Callback<Void> {
                                override fun onResponse(
                                    call: Call<Void>,
                                    response: Response<Void>
                                ) {
                                    Log.d(
                                        "LOKACIJA_PACIJENTA_REPO",
                                        "onResponse: ${response.code()}"
                                    )
                                }

                                override fun onFailure(call: Call<Void>, t: Throwable) {
                                    Log.d("LOKACIJA_PACIJENTA_REPO", "onFailure: ${t.message}")
                                }
                            })
                        Log.d(
                            LOCATION_SERVICE_TAG,
                            "SENDING LOCATION TO DB -> : USER_ID: ${lokacija.korisnikId}\n TIME: ${lokacija.vrijeme}"
                        )
                    }
                }
            }
        }
    }
}