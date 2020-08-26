package com.barisic.covid_19manager.viewmodels

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.barisic.covid_19manager.R
import com.barisic.covid_19manager.models.Poruka
import com.barisic.covid_19manager.repositories.JsonWebTokenRepository
import com.barisic.covid_19manager.repositories.PorukaRepository
import com.barisic.covid_19manager.util.*

class PorukaViewModel(
    application: Application,
    private val porukaRepo: PorukaRepository,
    private val jsonTokenRepo: JsonWebTokenRepository
) : ViewModel() {
    lateinit var lifecycleOwner: LifecycleOwner
    val message = InputValidator(MESSAGE)
    val response = MutableLiveData<Int?>()
    val closeDialog = MutableLiveData<Boolean>(false)
    val loading = MutableLiveData<Boolean>()
    private val jsonToken = MutableLiveData<String>()
    private val sharedPrefs = SharedPrefs(application.applicationContext)

    fun sendMessage() {
        if (loading.value != true) {
            if (message.validateInput()) {
                loading.value = true
                jsonTokenRepo.getJsonToken(jsonToken)
                if (!jsonToken.hasObservers()) {
                    jsonToken.observe(lifecycleOwner, Observer {
                        when (it) {
                            TOKEN_ACCESS_FAILED -> {
                                loading.value = false
                                response.value = R.string.connection_error
                            }
                            else -> porukaRepo.sendMessage(
                                it,
                                Poruka(
                                    sharedPrefs.getValueString(
                                        LOGGED_USER_ID
                                    )!!.toLong(),
                                    Common.getDateTimeLong(),
                                    message.inputValue.value!!
                                )
                            ) { responseCode ->
                                loading.value = false
                                when (responseCode) {
                                    CREATED -> {
                                        response.value = R.string.message_sent
                                        message.inputValue.value = ""
                                        closeDialog()
                                    }
                                    else -> response.value = R.string.sending_message_failed
                                }
                            }
                        }
                    })
                }
            }
        }
    }

    fun closeDialog() {
        closeDialog.value = true
    }
}