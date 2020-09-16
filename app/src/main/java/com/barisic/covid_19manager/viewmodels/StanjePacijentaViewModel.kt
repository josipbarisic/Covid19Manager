package com.barisic.covid_19manager.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.barisic.covid_19manager.R
import com.barisic.covid_19manager.models.StanjePacijenta
import com.barisic.covid_19manager.repositories.StanjePacijentaRepository
import com.barisic.covid_19manager.util.Common
import com.barisic.covid_19manager.util.LOGGED_USER_ID
import com.barisic.covid_19manager.util.SharedPrefs
import com.barisic.covid_19manager.util.UNIX_3H

class StanjePacijentaViewModel(
    private val context: Context,
    private val stanjePacijentaRepository: StanjePacijentaRepository
) : ViewModel() {
    lateinit var stateOnDayTitle: String
    private val sharedPrefs = SharedPrefs(context)

    var temperatureInteger = MutableLiveData(37)
    var temperatureDecimal = MutableLiveData(2)
    var temperature = MutableLiveData(
        Common.combineIntegersToFloatString(
            temperatureInteger.value!!,
            temperatureDecimal.value!!
        )
    )

    val coughBoolean = MutableLiveData(false)
    val fatigueBoolean = MutableLiveData(false)
    val musclePainBoolean = MutableLiveData(false)

    val showStanjePacijentaDialog = MutableLiveData(false)
    val showTemperaturePickerDialog = MutableLiveData(false)
    var loading = MutableLiveData(false)
    var errorMessage = MutableLiveData<Int?>()
    var result = MutableLiveData(false)

    fun submitState() {
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
            stanjePacijentaRepository.getStanjaPacijenta(
                sharedPrefs.getValueString(LOGGED_USER_ID).toString()
            ) {
                if (!it.isNullOrEmpty()
                    && !Common.isMoreThanGivenMillisDifference(it.last().vrijeme, UNIX_3H)
                ) {
                    Common.displayPopupErrorMessage(
                        R.string.less_than_3h_between_states,
                        loading,
                        errorMessage
                    )
                } else {
                    stanjePacijentaRepository.sendStanje(
                        StanjePacijenta(
                            sharedPrefs.getValueString(LOGGED_USER_ID)!!.toLong(),
                            temperature.value.toString().toFloat(),
                            getIntFromBoolean(coughBoolean.value!!),
                            getIntFromBoolean(fatigueBoolean.value!!),
                            getIntFromBoolean(musclePainBoolean.value!!),
                            Common.getDateTimeLong()
                        )
                    ) { responseSuccessful ->
                        if (responseSuccessful) {
                            loading.value = false
                            showStanjePacijentaDialog.value = false
                            result.value = true
                        } else
                            Common.displayPopupErrorMessage(
                                R.string.connection_error,
                                loading,
                                errorMessage
                            )
                    }
                }
            }
        }
    }

    fun getStringFromBoolean(value: Boolean): String {
        return if (value) {
            context.getString(R.string.yes)
        } else {
            context.getString(R.string.no)
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