package com.barisic.covid_19manager.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.barisic.covid_19manager.util.STANJE_PACIJENTA_UPWT
import com.barisic.covid_19manager.workers.StanjePacijentaAlertWorker
import timber.log.Timber
import java.util.concurrent.TimeUnit

class MainViewModel(application: Application) : ViewModel() {
    val workManager = WorkManager.getInstance(application.applicationContext)
    private val workRequest =
        PeriodicWorkRequestBuilder<StanjePacijentaAlertWorker>(
            PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS,
            TimeUnit.MILLISECONDS
        )
            .build()

    val callEpidemiologist = MutableLiveData(false)
    val cancelLogOut = MutableLiveData(false)
    val logOut = MutableLiveData(false)
    val probniTekst = MutableLiveData("")

    fun enqueueWork() {
        Timber.d("ALERT_WORKER")

        workManager.enqueueUniquePeriodicWork(
            STANJE_PACIJENTA_UPWT,
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )
    }

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