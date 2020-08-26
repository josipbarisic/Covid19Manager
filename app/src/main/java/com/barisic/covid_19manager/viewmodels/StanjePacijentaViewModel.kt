package com.barisic.covid_19manager.viewmodels

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.barisic.covid_19manager.R
import com.barisic.covid_19manager.models.StanjePacijenta
import com.barisic.covid_19manager.repositories.JsonWebTokenRepository
import com.barisic.covid_19manager.repositories.StanjePacijentaRepository
import com.barisic.covid_19manager.util.Common
import com.barisic.covid_19manager.util.LOGGED_USER_ID
import com.barisic.covid_19manager.util.SharedPrefs
import com.barisic.covid_19manager.util.TOKEN_ACCESS_FAILED

class StanjePacijentaViewModel(
    private val application: Application,
    private val jsonTokenRepository: JsonWebTokenRepository,
    private val stanjePacijentaRepository: StanjePacijentaRepository
) : ViewModel() {
    lateinit var stateOnDayTitle: String
    lateinit var lifecycleOwner: LifecycleOwner
    private var jsonToken = MutableLiveData<String>()
    var temperatureInteger = MutableLiveData<Int>().apply {
        this.value = 37
    }
    var temperatureDecimal = MutableLiveData<Int>().apply {
        this.value = 5
    }
    var temperature = MutableLiveData<String>().apply {
        this.value = Common.combineIntegersToFloatString(
            temperatureInteger.value!!,
            temperatureDecimal.value!!
        )
    }

    val coughBoolean = MutableLiveData<Boolean>().apply {
        this.value = false
    }
    val fatigueBoolean = MutableLiveData<Boolean>().apply {
        this.value = false
    }
    val musclePainBoolean = MutableLiveData<Boolean>().apply {
        this.value = false
    }

    val showStanjePacijentaDialog = MutableLiveData<Boolean>().apply {
        this.value = false
    }
    val showTemperaturePickerDialog = MutableLiveData<Boolean>().apply {
        this.value = false
    }
    var loading = MutableLiveData<Boolean>(false)
    var errorMessage = MutableLiveData<Int?>()

    fun submitState() {
        temperature.value = Common.combineIntegersToFloatString(
            temperatureInteger.value!!,
            temperatureDecimal.value!!
        )
        showStanjePacijentaDialog.value = true
    }

    fun closeStanjePacijentaDialog() {
        showStanjePacijentaDialog.value = false
    }

    fun closeTemperaturePickerDialog() {
        showTemperaturePickerDialog.value = false
    }

    fun showTemperaturePickerDialog() {
        showTemperaturePickerDialog.value = true
    }

    fun confirmState() {
        if (!loading.value!!) {
            loading.value = true
            jsonTokenRepository.getJsonToken(jsonToken)
            if (!jsonToken.hasObservers()) {
                jsonToken.observe(lifecycleOwner, Observer {
                    when (it) {
                        TOKEN_ACCESS_FAILED -> {
                            println("TOKEN RESPONSE ----> NULL")
                        }
                        else -> {
                            stanjePacijentaRepository.sendStanje(
                                it,
                                StanjePacijenta(
                                    SharedPrefs(application.applicationContext).getValueString(
                                        LOGGED_USER_ID
                                    )!!.toLong(),
                                    temperature.value.toString().toFloat(),
                                    getIntFromBoolean(coughBoolean.value!!),
                                    getIntFromBoolean(fatigueBoolean.value!!),
                                    getIntFromBoolean(musclePainBoolean.value!!),
                                    Common.getDateTimeLong()
                                ),
                                loading,
                                errorMessage,
                                showStanjePacijentaDialog
                            )
                            jsonToken.removeObservers(lifecycleOwner)
                        }

                    }
                })
            }
        }
    }

    fun getStringFromBoolean(value: Boolean): String {
        return if (value) {
            application.getString(R.string.yes)
        } else {
            application.getString(R.string.no)
        }
    }

    private fun getIntFromBoolean(value: Boolean): Int {
        return if (value) {
            1
        } else {
            0
        }
    }
}