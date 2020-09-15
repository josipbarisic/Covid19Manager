package com.barisic.covid_19manager.services

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.media.RingtoneManager
import android.os.IBinder
import android.os.Looper
import com.barisic.covid_19manager.models.LokacijaPacijenta
import com.barisic.covid_19manager.repositories.LokacijaPacijentaRepository
import com.barisic.covid_19manager.util.*
import com.google.android.gms.location.*
import org.koin.android.ext.android.get
import timber.log.Timber

class LocationService : Service() {
    companion object {
        private const val UPDATE_INTERVAL_MS: Long = 1000 * 60 * 15
        private const val FASTEST_UPDATE_INTERVAL_MS: Long = UPDATE_INTERVAL_MS / 2
    }

    private var locationRequest: LocationRequest? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var locationCallback: LocationCallback? = null
    private var location: Location? = null
    private var isUserLogged: Boolean = false
    private var userId: Long? = null
    private var userLat: Double? = null
    private var userLong: Double? = null

    override fun onCreate() {
        Timber.tag(LOCATION_SERVICE_TAG).d("onCreate: CREATED")
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        Timber.tag(LOCATION_SERVICE_TAG).d("onStartLocationUpdates: STARTED")

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(applicationContext)
        createLocationRequest()
        createLocationCallback()

        fusedLocationProviderClient!!.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.tag(LOCATION_SERVICE_TAG).d("onStartCommand: STARTED LOCATION SERVICE")
        SharedPrefs(applicationContext).save(LOCATION_SERVICE_RUNNING, true)
        startLocationUpdates()
        return START_STICKY
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest.create()
        locationRequest!!.interval = UPDATE_INTERVAL_MS
        locationRequest!!.smallestDisplacement = 1.0f
        locationRequest!!.fastestInterval = FASTEST_UPDATE_INTERVAL_MS
        locationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private fun createLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                isUserLogged = SharedPrefs(applicationContext).getValueBoolean(LOGGED_USER, false)

                if (isUserLogged && locationResult != null && locationResult.lastLocation != null) {
                    location = locationResult.lastLocation
                    userId =
                        SharedPrefs(applicationContext).getValueString(LOGGED_USER_ID)!!.toLong()
                    userLat =
                        SharedPrefs(applicationContext).getValueString(LOGGED_USER_LAT)!!.toString()
                            .toDouble()
                    userLong =
                        SharedPrefs(applicationContext).getValueString(LOGGED_USER_LONG)!!
                            .toString()
                            .toDouble()
                    Timber.tag(LOCATION_SERVICE_TAG).d(
                        "LOCATION RECEIVED -> : ${Common.getLocationText(location)}"
                    )
                    Timber.tag(LOCATION_SERVICE_TAG).d(
                        "onLocationResult: SHARED_PREFS USER LOGGED IN -> ${
                            SharedPrefs(
                                applicationContext
                            )
                                .getValueBoolean(LOGGED_USER, false)
                        }"
                    )
                    LokacijaPacijentaRepository(get(), get()).sendLokacija(
                        LokacijaPacijenta(
                            userId!!,
                            location!!.latitude.toFloat(),
                            location!!.longitude.toFloat(),
                            Common.getDateTimeLong()
                        )
                    )

                    if (Common.getDistanceBetweenLocations(
                            userLat!!,
                            userLong!!,
                            location!!.latitude,
                            location!!.longitude
                        ) > 1000f
                    ) {
                        val tone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                        val ringtone = RingtoneManager.getRingtone(applicationContext, tone)
                        if (!ringtone.isPlaying) {
                            ringtone.play()
                        }
                        NotificationHelper.showDistanceWarningNotification(applicationContext)
                    } else {
                        val notificationManager =
                            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        notificationManager.cancelAll()
                    }
                } else {
                    userLat = null
                    userLong = null
                }
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        SharedPrefs(applicationContext).save(LOCATION_SERVICE_RUNNING, false)
    }
}