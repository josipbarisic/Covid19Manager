package com.barisic.covid_19manager.util

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Handler
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import com.barisic.covid_19manager.activities.BaseActivity
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object Common {
    fun getLocationText(mLocation: Location?): String {
        return if (mLocation == null) {
            "Unknown Location"
        } else {
            "${mLocation.latitude}/${mLocation.longitude}"
        }
    }

    fun getLocationTitle(): String {
        return String.format("Location Updated: ", DateFormat.getDateInstance().format(Date()))
    }

    @SuppressLint("SimpleDateFormat")
    fun getDate(): String {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy.")
        val date = Calendar.getInstance().time

        return dateFormat.format(date)
    }

    @SuppressLint("SimpleDateFormat")
    fun getDbDate(): Int {
        val dateFormat = SimpleDateFormat("yyyyMMdd")
        val date = Calendar.getInstance().time

        return dateFormat.format(date).toInt()
    }

    @SuppressLint("SimpleDateFormat")
    fun getDateTimeLong(): Long {
        val dateFormat = SimpleDateFormat("yyyyMMddHHmm")
        val date = Calendar.getInstance().time

        return dateFormat.format(date).toLong()
    }

    fun getDistanceBetweenLocations(
        first_lat: Double,
        first_long: Double,
        second_lat: Double,
        second_long: Double
    ): Float {
        val firstLocation = Location("")
        firstLocation.latitude = first_lat
        firstLocation.longitude = first_long
        val secondLocation = Location("")
        secondLocation.latitude = second_lat
        secondLocation.longitude = second_long

        Log.d(
            "DISTANCE_IN_METERS",
            "getDistanceBetweenLocations: ${firstLocation.distanceTo(secondLocation)}"
        )
        return firstLocation.distanceTo(secondLocation)
    }

    fun displayPopupErrorMessage(
        resource: Int,
        loading: MutableLiveData<Int?>,
        errorMessage: MutableLiveData<Int?>
    ) {
        loading.value = null
        errorMessage.value = resource

        //Hide after 3s
        Handler().postDelayed({
            errorMessage.value = null
        }, 3000)
    }

    fun combineIntegersToFloatString(valueInt: Int, valueDecimal: Int): String {
        return "$valueInt.$valueDecimal"
    }

    fun makeEmergencyCall(activity: BaseActivity) {
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.CALL_PHONE),
                REQUEST_CALL
            )
        } else {
            activity.startActivity(
                Intent(
                    Intent.ACTION_CALL,
                    Uri.parse("tel:" + "+385976485912")
                )
            )
        }
    }

}