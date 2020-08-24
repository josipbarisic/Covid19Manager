package com.barisic.covid_19manager.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    val callEpidemiologist = MutableLiveData<Boolean>(false)
    val cancelLogOut = MutableLiveData<Boolean>(false)
    val logOut = MutableLiveData<Boolean>(false)

    fun callClosestEpidemiologist() {
        callEpidemiologist.value = true
    }

    fun cancelLogOut() {
        cancelLogOut.value = true
    }

    fun logOut() {
        logOut.value = true
    }
}