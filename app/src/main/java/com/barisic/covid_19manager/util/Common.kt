package com.barisic.covid_19manager.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Handler
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import com.barisic.covid_19manager.R
import com.barisic.covid_19manager.activities.BaseActivity
import com.barisic.covid_19manager.models.Epidemiolog
import timber.log.Timber
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

    @SuppressLint("SimpleDateFormat")
    fun parseLongDateToString(date: Long): String {
        val inDateFormat = SimpleDateFormat("yyyyMMddHHmm")
        val outDateFormat = SimpleDateFormat("dd.MM.yyyy. HH:mm")
        val nonParsedDate: Date? = inDateFormat.parse(date.toString())

        return if (nonParsedDate != null) {
            outDateFormat.format(nonParsedDate)
        } else {
            "N/A"
        }
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

        Timber.tag("DISTANCE_IN_METERS").d(
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

    fun getStringFromInt(value: Int): String {
        return if (value == 1) {
            "Da"
        } else {
            "Ne"
        }
    }

    fun showAlertDialog(
        ctx: Context,
        message: Int,
        positiveButtonText: Int,
        positiveButtonFunction: () -> Unit,
        negativeButtonText: Int,
        title: Int
    ) {
        AlertDialog.Builder(ctx, R.style.WhiteDialogBackgroundStyle)
            .setMessage(message)
            .setPositiveButton(positiveButtonText) { _, _ ->
                positiveButtonFunction()
            }
            .setCancelable(false)
            .setNegativeButton(negativeButtonText, null)
            .setTitle(title)
            .show()
    }

    fun getClosestEpidemiologist(
        lat: Float,
        long: Float,
        epidemiologists: ArrayList<Epidemiolog>
    ): String {
        var shortestLength = Float.MAX_VALUE
        var thisLength: Float
        var currentClosest: Epidemiolog? = null
        for (epidemiolog: Epidemiolog in epidemiologists) {
            thisLength = getDistanceBetweenLocations(
                lat.toDouble(),
                long.toDouble(),
                epidemiolog.lat.toDouble(),
                epidemiolog.long.toDouble()
            )
            if (shortestLength > thisLength) {
                shortestLength = thisLength
                currentClosest = epidemiolog
            }
        }
        return currentClosest?.brojTelefona ?: HZJZ_PHONE
    }
}