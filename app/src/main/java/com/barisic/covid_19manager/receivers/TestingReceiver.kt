package com.barisic.covid_19manager.receivers

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import com.barisic.covid_19manager.util.LOCATION_SERVICE_RUNNING
import com.barisic.covid_19manager.util.SharedPrefs

class TestingReceiver : BroadcastReceiver() {
    var player: MediaPlayer? = null

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context?, intent: Intent?) {
        /*if(Intent.ACTION_USER_PRESENT == intent!!.action){
            Log.d("TESTING_RECEIVER22", "onReceive: RECEIVED")

        }*/
        /*when(intent!!.action){
            ACTION_START_NOTIFICATION -> {
                val periodicWorkRequest = PeriodicWorkRequest.Builder(ReminderWorker::class.java, Duration.ofMinutes(15)).build()
                WorkManager.getInstance(context!!).enqueueUniquePeriodicWork("REMINDER_WORK", ExistingPeriodicWorkPolicy.REPLACE, periodicWorkRequest)
            }
        }*/
        if (Intent.ACTION_SCREEN_OFF == intent!!.action) {
            Log.d("TESTING_RECEIVER1", "onReceive: RECEIVED")
            Log.d(
                "TESTING_RECEIVER1", "onReceive: ${SharedPrefs(context!!).getValueBoolean(
                    LOCATION_SERVICE_RUNNING, false
                )}"
            )
            player = MediaPlayer.create(
                context,
                Settings.System.DEFAULT_ALARM_ALERT_URI
            )
            player!!.start()
        }
    }
}