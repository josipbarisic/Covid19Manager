package com.barisic.covid_19manager.repositories

import androidx.lifecycle.MutableLiveData
import com.barisic.covid_19manager.R
import com.barisic.covid_19manager.interfaces.StanjePacijentaInterface
import com.barisic.covid_19manager.models.StanjePacijenta
import com.barisic.covid_19manager.util.Common.displayPopupErrorMessage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class StanjePacijentaRepository(private val stanjePacijentaService: StanjePacijentaInterface) {
    fun sendStanje(
        token: String,
        stanje: StanjePacijenta,
        loading: MutableLiveData<Boolean>,
        errorMessage: MutableLiveData<Int?>,
        showDialogFragment: MutableLiveData<Boolean>
    ) {
        Timber.tag("SEND_STANJE").d(stanje.toString())
        stanjePacijentaService.postStanjePacijenta(token, stanje).enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Timber.tag("STANJE_RESPONSE").d("onFailure: ${t.message}")
                loading.value = false
                displayPopupErrorMessage(R.string.connection_error, loading, errorMessage)
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Timber.tag("STANJE_RESPONSE").d("onResponse: ${response.headers()}")
                loading.value = false
                showDialogFragment.value = false
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