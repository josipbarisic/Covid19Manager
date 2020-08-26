package com.barisic.covid_19manager.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.barisic.covid_19manager.R
import com.barisic.covid_19manager.models.StanjePacijenta
import com.barisic.covid_19manager.repositories.JsonWebTokenRepository
import com.barisic.covid_19manager.repositories.StanjePacijentaRepository
import com.barisic.covid_19manager.util.LOGGED_USER_ID
import com.barisic.covid_19manager.util.SharedPrefs
import com.barisic.covid_19manager.util.TOKEN_ACCESS_FAILED

class PovijestStanjaViewModel(
    private val application: Application,
    private val jsonTokenRepository: JsonWebTokenRepository,
    private val stanjePacijentaRepository: StanjePacijentaRepository
) : ViewModel() {
    private val jsonToken = MutableLiveData<String>()
    lateinit var lifecycleOwner: LifecycleOwner

    private val povijestStanjaPacijentaMLD = MutableLiveData<ArrayList<StanjePacijenta>>()
    val povijestStanjaPacijenta: LiveData<ArrayList<StanjePacijenta>>
        get() = povijestStanjaPacijentaMLD
    val loading = MutableLiveData<Boolean>()
    val showPlaceHolder = MutableLiveData<Boolean>(false)
    val placeHolderText = MutableLiveData<String>()
    val errorMessage = MutableLiveData<Int?>()

    fun getStanjaPacijenta() {
        loading.value = true
        jsonTokenRepository.getJsonToken(jsonToken)
        if (!jsonToken.hasObservers()) {
            jsonToken.observe(lifecycleOwner, Observer {
                when (it) {
                    TOKEN_ACCESS_FAILED -> {
                        println("TOKEN RESPONSE ----> NULL")
                    }
                    else -> {
                        stanjePacijentaRepository.getStanjaPacijenta(
                            it,
                            SharedPrefs(application.applicationContext).getValueString(
                                LOGGED_USER_ID
                            )!!
                        ) { stanjaPacijenta: ArrayList<StanjePacijenta>? ->
                            loading.value = false
                            when {
                                stanjaPacijenta == null -> {
                                    errorMessage.value = R.string.connection_error
                                    placeHolderText.value =
                                        application.resources.getString(R.string.medical_records_error)
                                    showPlaceHolder.value = true
                                }
                                stanjaPacijenta.isEmpty() -> {
                                    placeHolderText.value =
                                        application.resources.getString(R.string.medical_records_empty)
                                    showPlaceHolder.value = true
                                }
                                else -> povijestStanjaPacijentaMLD.value = stanjaPacijenta
                            }
                        }
                    }
                }
            })
        }
    }
}