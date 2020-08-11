package com.barisic.covid_19manager.services

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.media.MediaPlayer
import android.os.IBinder
import android.os.Looper
import android.provider.Settings
import android.util.Log
import com.barisic.covid_19manager.models.LokacijaPacijenta
import com.barisic.covid_19manager.repositories.LokacijaPacijentaRepository
import com.barisic.covid_19manager.util.*
import com.google.android.gms.location.*
import org.koin.android.ext.android.get

class LocationService : Service() {
    companion object {
        private const val UPDATE_INTERVAL_MS: Long = 1000 * 60 * 5
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

    var player: MediaPlayer? = null

    override fun onCreate() {
        Log.d(LOCATION_SERVICE_TAG, "onCreate: CREATED")
        player = MediaPlayer.create(
            applicationContext,
            Settings.System.DEFAULT_ALARM_ALERT_URI
        )
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        Log.d(LOCATION_SERVICE_TAG, "onStartLocationUpdates: STARTED")

        createLocationRequest()
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(applicationContext)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                isUserLogged = SharedPrefs(applicationContext).getValueBoolean(LOGGED_USER, false)

                Log.d("COORDINATES", "getLocations: $userLat / $userLong")

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
                    Log.d(
                        LOCATION_SERVICE_TAG,
                        "LOCATION RECEIVED -> : ${Common.getLocationText(location)}"
                    )
                    Log.d(
                        LOCATION_SERVICE_TAG,
                        "onLocationResult: SHARED_PREFS USER LOGGED IN -> ${SharedPrefs(
                            applicationContext
                        )
                            .getValueBoolean(LOGGED_USER, false)}"
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
                        if (!player!!.isPlaying) {
                            player!!.start()

                        }
                        NotificationHelper.showWarningNotification(applicationContext)
                    } else {
                        val notificationManager =
                            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        notificationManager.cancelAll()
                    }
                    Log.d(LOCATION_SERVICE_TAG, "GETTING TOKEN ...")
                } else {
                    userLat = null
                    userLong = null
                }
            }
        }
        fusedLocationProviderClient!!.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(LOCATION_SERVICE_TAG, "onStartCommand: STARTED LOCATION SERVICE")
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

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        SharedPrefs(applicationContext).save(LOCATION_SERVICE_RUNNING, false)
    }
}