package com.barisic.covid_19manager.viewmodels

import android.content.Context
import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.barisic.covid_19manager.R
import com.barisic.covid_19manager.models.StanjePacijenta
import com.barisic.covid_19manager.repositories.StanjePacijentaRepository
import com.barisic.covid_19manager.util.LOGGED_USER_ID
import com.barisic.covid_19manager.util.SharedPrefs

class PovijestStanjaViewModel(
    private val context: Context,
    private val stanjePacijentaRepository: StanjePacijentaRepository
) : ViewModel() {
    private val sharedPrefs = SharedPrefs(context)
    private val povijestStanjaPacijentaMLD = MutableLiveData<ArrayList<StanjePacijenta>>()
    val povijestStanjaPacijenta: LiveData<ArrayList<StanjePacijenta>>
        get() = povijestStanjaPacijentaMLD
    val loading = MutableLiveData<Boolean>()
    val showPlaceHolder = MutableLiveData(false)
    val placeHolderText = MutableLiveData<String>()
    val errorMessage = MutableLiveData<Int?>()

    fun getStanjaPacijenta() {
        loading.value = true
        stanjePacijentaRepository.getStanjaPacijenta(
            sharedPrefs.getValueString(
                LOGGED_USER_ID
            )!!
        ) { stanjaPacijenta: ArrayList<StanjePacijenta>? ->
            when {
                stanjaPacijenta == null -> {
                    errorMessage.value = R.string.connection_error
                    placeHolderText.value = context.getString(R.string.medical_records_error)
                    showPlaceHolder.value = true
                    loading.value = false
                }
                stanjaPacijenta.isEmpty() -> {
                    placeHolderText.value =
                        context.getString(R.string.medical_records_empty)
                    showPlaceHolder.value = true
                    loading.value = false
                }
                else -> Handler().postDelayed({
                    loading.value = false
                    povijestStanjaPacijentaMLD.value = stanjaPacijenta
                }, 1000)
            }
        }
    }
}