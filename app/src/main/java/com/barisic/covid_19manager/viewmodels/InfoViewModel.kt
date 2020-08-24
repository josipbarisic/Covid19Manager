package com.barisic.covid_19manager.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class InfoViewModel : ViewModel() {
    val loading = MutableLiveData<Boolean>(true)
}