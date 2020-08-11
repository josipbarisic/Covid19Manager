package com.barisic.covid_19manager.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.barisic.covid_19manager.util.LOADING

class InfoViewModel : ViewModel() {
    var loading = MutableLiveData<Int?>(LOADING)
}