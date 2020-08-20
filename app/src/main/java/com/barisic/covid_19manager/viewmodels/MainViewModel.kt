package com.barisic.covid_19manager.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    val cancelLogOut = MutableLiveData<Boolean>(false)
    val logOut = MutableLiveData<Boolean>(false)

    fun cancelLogOut() {
        cancelLogOut.value = true
    }

    fun logOut() {
        logOut.value = true
    }
}