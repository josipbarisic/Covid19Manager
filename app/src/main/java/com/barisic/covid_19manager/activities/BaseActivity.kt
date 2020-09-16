package com.barisic.covid_19manager.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.barisic.covid_19manager.R
import com.barisic.covid_19manager.services.LocationService
import com.barisic.covid_19manager.util.Common.showAlertDialog
import com.barisic.covid_19manager.util.LOCATION_SERVICE_TAG
import com.barisic.covid_19manager.util.LOGGED_USER
import com.barisic.covid_19manager.util.SharedPrefs
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import timber.log.Timber

abstract class BaseActivity : AppCompatActivity() {
    private var locationManager: LocationManager? = null
    var locationServiceIntent: Intent? = null
    private lateinit var sharedPrefs: SharedPrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.tag(LOCATION_SERVICE_TAG)
            .d("IS_RUNNING in BASE activity -> ${LocationService.isServiceRunning()}")
        locationServiceIntent = Intent(applicationContext, LocationService::class.java)
        sharedPrefs = SharedPrefs(applicationContext)
        handleLoginPrefs()
        locationManager =
            applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    @SuppressLint("InlinedApi")
    fun checkLocationPermissions() {
        if (locationManager != null
            && !locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
            && !locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        ) {
            showAlertDialog(
                this,
                R.string.gps_network_not_enabled,
                R.string.open_location_settings,
                {
                    startActivity(
                        Intent(
                            Settings.ACTION_LOCATION_SOURCE_SETTINGS
                        )
                    )
                },
                R.string.cancel,
                R.string.app_name
            )
        }

        Dexter.withActivity(this)
            .withPermissions(
                listOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            )
            .withListener(object : MultiplePermissionsListener {
                @SuppressLint("MissingPermission")
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (SharedPrefs(applicationContext).getValueBoolean(
                            LOGGED_USER,
                            false
                        )
                        && checkPermission(
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                        && checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    ) {
                        locationServiceIntent!!.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                        if (!LocationService.isServiceRunning()) {
                            startService(locationServiceIntent)
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.gps_network_not_enabled),
                        Toast.LENGTH_LONG
                    ).show()
                }

            }).check()
    }

    fun handleLoginPrefs(): Boolean {
        if (sharedPrefs.getValueBoolean(
                LOGGED_USER,
                false
            ) && this.javaClass.simpleName == "LoginActivity"
        ) {
            val i = Intent(this, MainActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(i)
            finish()
        } else if (!sharedPrefs.getValueBoolean(
                LOGGED_USER,
                false
            ) && this.javaClass.simpleName == "MainActivity"
        ) {
            val i = Intent(this, LoginActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(i)
            finish()
        }

        return true
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            applicationContext,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }
}