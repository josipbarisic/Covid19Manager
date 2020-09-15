package com.barisic.covid_19manager.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.barisic.covid_19manager.repositories.StanjePacijentaRepository
import com.barisic.covid_19manager.util.Common
import com.barisic.covid_19manager.util.LOGGED_USER_ID
import com.barisic.covid_19manager.util.NotificationHelper
import com.barisic.covid_19manager.util.SharedPrefs
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber

class StanjePacijentaAlertWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams), KoinComponent {

    private val stanjePacijentaRepo: StanjePacijentaRepository by inject()

    override fun doWork(): Result {

        Timber.d("STANJE_PACIJENTA_ALERT_WORKER_WORKING")
        stanjePacijentaRepo.getStanjaPacijenta(
            SharedPrefs(applicationContext).getValueString(
                LOGGED_USER_ID
            )!!
        ) {
            it?.let {
                if (it.isNotEmpty() &&
                    Common.isMoreThan12HDifference(it.last().vrijeme)
                ) {
                    NotificationHelper.showStateUpdateWarningNotification(applicationContext)
                }
            }
        }
        return Result.success()
    }
}