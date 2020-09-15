package com.barisic.covid_19manager.util

import android.annotation.TargetApi
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import com.barisic.covid_19manager.R
import com.barisic.covid_19manager.activities.MainActivity
import com.barisic.covid_19manager.receivers.EpidemiologistPhoneNumberReceiver

class NotificationHelper {

    companion object {
        private const val CHANNEL_ID = "COVID-19_channel_01"
        private const val CHANNEL_NAME = "COVID-19 Manager"
        private const val COVID_19_DISTANCE_WARNING_NID = 251098
        private const val COVID_19_EPIDEMIOLOGIST_NID = 251099
        private const val COVID_19_STATE_WARNING_NID = 251020


        fun showDistanceWarningNotification(context: Context) {
            val builder = getBasicNotificationBuilder(context)
            builder.setContentTitle(context.getString(R.string.warning))
                .setContentText(context.getString(R.string.distance_warning_message))
                .setOngoing(false)

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(CHANNEL_ID, CHANNEL_NAME, true)

            notificationManager.notify(COVID_19_DISTANCE_WARNING_NID, builder.build())
        }

        fun showEpidemiologistNotification(context: Context) {
            val sharedPrefs = SharedPrefs(context)
            val zzjzName = sharedPrefs.getValueString(LOGGED_USER_EPIDEMIOLOGIST_ZZJZ)
            val phoneNumber = sharedPrefs.getValueString(LOGGED_USER_EPIDEMIOLOGIST_NUMBER)
            val message =
                "ZZJZ: $zzjzName\n${context.getString(R.string.phone_number)}: $phoneNumber"

            val numberCopyIntent = Intent(context, EpidemiologistPhoneNumberReceiver::class.java)
            numberCopyIntent.action = ACTION_COPY_PHONE_NUMBER
            numberCopyIntent.putExtra("phone_number", phoneNumber)

            val pendingIntent =
                PendingIntent.getBroadcast(
                    context,
                    0,
                    numberCopyIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            val builder = getBasicNotificationBuilder(context)
            builder.setContentTitle(context.getString(R.string.calling_closest_epidemiologist))
                .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                .setContentText(message)
                .setOngoing(false)
                .setDefaults(Notification.DEFAULT_SOUND)
                .addAction(
                    R.drawable.ic_emergency_call,
                    context.getString(R.string.copy_epidemiologist_number),
                    pendingIntent
                )

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(CHANNEL_ID, CHANNEL_NAME, true)

            notificationManager.notify(COVID_19_EPIDEMIOLOGIST_NID, builder.build())
        }

        fun showStateUpdateWarningNotification(context: Context) {
            val builder = getBasicNotificationBuilder(context)
            @Suppress("SameParameterValue")
            builder.setContentTitle(context.getString(R.string.update_state))
                .setContentText(context.getString(R.string.more_than_12_hours_passed))
                .setOngoing(false)
                .setDefaults(Notification.DEFAULT_SOUND)

            val appProcessInfo = ActivityManager.RunningAppProcessInfo()
            ActivityManager.getMyMemoryState(appProcessInfo)
            if (appProcessInfo.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                builder.setContentIntent(
                    getPendingIntentWithStack(
                        context,
                        MainActivity::class.java,
                        ACTION_OPEN_STANJE_PACIJENTA
                    )
                )
            }

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(CHANNEL_ID, CHANNEL_NAME, true)

            notificationManager.notify(COVID_19_STATE_WARNING_NID, builder.build())
        }

        private fun getBasicNotificationBuilder(
            context: Context
        ): NotificationCompat.Builder {
            return NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.app_logo)
                .setAutoCancel(true)
                .setDefaults(0)
        }

        private fun <T> getPendingIntentWithStack(
            context: Context,
            javaClass: Class<T>,
            intentAction: String?
        ): PendingIntent? {
            val resultIntent = Intent(context, javaClass)
            intentAction?.let {
                resultIntent.action = it
            }
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
            val tone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channelImportance = if (playSound) NotificationManager.IMPORTANCE_HIGH
                else NotificationManager.IMPORTANCE_DEFAULT
                val nChannel = NotificationChannel(channelId, channelName, channelImportance)
                nChannel.enableLights(true)
                nChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                nChannel.enableVibration(true)
                nChannel.lightColor = Color.BLUE
                if (playSound) {
                    nChannel.setSound(
                        tone,
                        AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION)
                            .build()
                    )
                }
                this.createNotificationChannel(nChannel)
            }
        }
    }
}