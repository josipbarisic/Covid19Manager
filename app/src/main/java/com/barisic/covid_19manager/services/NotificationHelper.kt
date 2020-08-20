package com.barisic.covid_19manager.services

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import com.barisic.covid_19manager.R
import com.barisic.covid_19manager.receivers.TestingReceiver
import com.barisic.covid_19manager.util.ACTION_START_NOTIFICATION

class NotificationHelper {

    companion object {
        private const val CHANNEL_ID = "COVID-19_channel_01"
        private const val CHANNEL_NAME = "COVID-19 Manager"
        private const val COVID_19_NID = 251098


        fun showWarningNotification(context: Context) {
            val startIntent = Intent(context, TestingReceiver::class.java)
            startIntent.action = ACTION_START_NOTIFICATION
            val startPendingIntent =
                PendingIntent.getBroadcast(
                    context,
                    0,
                    startIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            val builder = getBasicNotificationBuilder(context, CHANNEL_ID, true)
            builder.setContentTitle("Upozorenje!")
                .setContentText("Udaljeni ste više od 1km od mjesta prebivališta!")
                .setOngoing(false)
                .setDefaults(Notification.DEFAULT_SOUND)
//                .setContentIntent(getPendingIntentWithStack(context, MainActivity::class.java))
//                .addAction(R.drawable.ic_logout, "Start", startPendingIntent)

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(CHANNEL_ID, CHANNEL_NAME, true)

            notificationManager.notify(COVID_19_NID, builder.build())
        }

        private fun getBasicNotificationBuilder(
            context: Context,
            channelId: String,
            playSound: Boolean
        ): NotificationCompat.Builder {
            val notificationSound: Uri =
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val nBuilder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.app_logo)
                .setAutoCancel(true)
                .setDefaults(0)
            if (playSound) nBuilder.setSound(notificationSound)
            return nBuilder
        }

        private fun <T> getPendingIntentWithStack(
            context: Context,
            javaClass: Class<T>
        ): PendingIntent? {
            val resultIntent = Intent(context, javaClass)
            resultIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

            val stackBuilder = TaskStackBuilder.create(context)
            stackBuilder.addParentStack(javaClass)
            stackBuilder.addNextIntent(resultIntent)

            return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        @TargetApi(26)
        private fun NotificationManager.createNotificationChannel(
            channelId: String,
            channelName: String,
            playSound: Boolean
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channelImportance = if (playSound) NotificationManager.IMPORTANCE_HIGH
                else NotificationManager.IMPORTANCE_LOW
                val nChannel = NotificationChannel(channelId, channelName, channelImportance)
                nChannel.enableLights(true)
                nChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                nChannel.enableVibration(true)
                nChannel.lightColor = Color.BLUE
                this.createNotificationChannel(nChannel)
            }
        }
    }
}