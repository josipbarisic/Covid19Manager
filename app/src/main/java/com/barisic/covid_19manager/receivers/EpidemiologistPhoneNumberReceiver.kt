package com.barisic.covid_19manager.receivers

import android.annotation.SuppressLint
import android.content.*
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi


class EpidemiologistPhoneNumberReceiver : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null) {
            val clipboardManager =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip =
                ClipData.newPlainText("Broj epidemiologa", intent.extras?.getString("phone_number"))
            clipboardManager.setPrimaryClip(clip)
            Toast.makeText(context, "Broj kopiran!", Toast.LENGTH_SHORT).show()
        }
    }
}