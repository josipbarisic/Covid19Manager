package com.barisic.covid_19manager.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.barisic.covid_19manager.R
import com.barisic.covid_19manager.models.Poruka
import com.barisic.covid_19manager.repositories.PorukaRepository
import com.barisic.covid_19manager.util.*

class PorukaViewModel(
    context: Context,
    private val porukaRepo: PorukaRepository
) : ViewModel() {
    val message = InputValidator(MESSAGE)
    val response = MutableLiveData<Int?>()
    val closeDialog = MutableLiveData(false)
    val loading = MutableLiveData(false)
    private val sharedPrefs = SharedPrefs(context)

    fun sendMessage() {
        if (loading.value != true) {
            if (message.validateInput()) {
                loading.value = true

                porukaRepo.sendMessage(
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
        }
    }

    fun closeDialog() {
        closeDialog.value = true
    }
}