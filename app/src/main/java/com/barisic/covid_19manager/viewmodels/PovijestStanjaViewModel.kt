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
    var application: Application,
    private val jsonTokenRepository: JsonWebTokenRepository,
    private val stanjePacijentaRepository: StanjePacijentaRepository
) : ViewModel() {
    private var jsonToken = MutableLiveData<String>()
    lateinit var lifecycleOwner: LifecycleOwner

    private val povijestStanjaPacijentaMLD = MutableLiveData<ArrayList<StanjePacijenta>>()
    val povijestStanjaPacijenta: LiveData<ArrayList<StanjePacijenta>>
        get() = povijestStanjaPacijentaMLD
    val loading = MutableLiveData<Boolean>()
    val showPlaceHolder = MutableLiveData<Boolean>(false)
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
                                }
                                stanjaPacijenta.isEmpty() -> {
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

    fun getStringFromInt(value: Int): String {
        return if (value == 1) {
            application.getString(R.string.yes)
        } else {
            application.getString(R.string.no)
        }
    }
}