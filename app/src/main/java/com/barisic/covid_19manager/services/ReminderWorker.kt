package com.barisic.covid_19manager.services

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class ReminderWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    override fun doWork(): Result {
        NotificationHelper.showWarningNotification(applicationContext)
        /*val stanjePacijentaRepository = StanjePacijentaRepository(get().koin.get())
        Log.d("REMINDER_WORKER", "doWork: $stanjePacijentaRepository")*/
        return Result.success()
    }
}