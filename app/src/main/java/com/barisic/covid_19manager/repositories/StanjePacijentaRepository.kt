package com.barisic.covid_19manager.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.barisic.covid_19manager.R
import com.barisic.covid_19manager.interfaces.StanjePacijentaInterface
import com.barisic.covid_19manager.models.StanjePacijenta
import com.barisic.covid_19manager.util.Common.displayPopupErrorMessage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class StanjePacijentaRepository(private val stanjePacijentaService: StanjePacijentaInterface) {
    fun sendStanje(
        token: String,
        stanje: StanjePacijenta,
        loading: MutableLiveData<Int?>,
        errorMessage: MutableLiveData<Int?>,
        showDialogFragment: MutableLiveData<Boolean>
    ) {
        stanjePacijentaService.postStanjePacijenta(token, stanje).enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d("STANJE_RESPONSE", "onFailure: ${t.message}")
                loading.value = null
                displayPopupErrorMessage(R.string.connection_error, loading, errorMessage)
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Log.d("STANJE_RESPONSE", "onResponse: ${response.headers()}")
                loading.value = null
                showDialogFragment.value = false
            }

        })
    }

    fun getStanjaPacijenta(token: String, id: String) {
        stanjePacijentaService.getStanjaById(token, id)
            .enqueue(object : Callback<ArrayList<StanjePacijenta>> {
                override fun onResponse(
                    call: Call<ArrayList<StanjePacijenta>>,
                    response: Response<ArrayList<StanjePacijenta>>
                ) {
                    for (stanje: StanjePacijenta in response.body()!!) {
                        Log.d(
                            "ALL_STATES",
                            "onResponse: Pacijent -> ${stanje.korisnikId} Vrijeme -> ${stanje.vrijeme}"
                        )
                    }

                }

                override fun onFailure(call: Call<ArrayList<StanjePacijenta>>, t: Throwable) {
                    Log.d("ALL_STATES", "onFailure: FAILED -> ${t.message}")
                }
            })
    }
}