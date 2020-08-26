package com.barisic.covid_19manager.repositories

import androidx.lifecycle.MutableLiveData
import com.barisic.covid_19manager.interfaces.LokacijaPacijentaInterface
import com.barisic.covid_19manager.models.LokacijaPacijenta
import com.barisic.covid_19manager.util.LOCATION_SERVICE_TAG
import com.barisic.covid_19manager.util.TOKEN_ACCESS_FAILED
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

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
                                    Timber.tag(LOCATION_SERVICE_TAG)
                                        .d("onResponse: ${response.code()}")
                                }

                                override fun onFailure(call: Call<Void>, t: Throwable) {
                                    Timber.tag(LOCATION_SERVICE_TAG).d("onFailure: ${t.message}")
                                }
                            })
                        Timber.tag(LOCATION_SERVICE_TAG)
                            .d("SENDING LOCATION TO DB -> : USER_ID: ${lokacija.korisnikId}\n TIME: ${lokacija.vrijeme}")
                    }
                }
            }
        }
    }
}