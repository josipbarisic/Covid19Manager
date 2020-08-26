package com.barisic.covid_19manager.receivers

import android.content.*
import android.widget.Toast


class EpidemiologistPhoneNumberReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null) {
            val clipboardManager =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip =
                ClipData.newPlainText("Broj epidemiologa", intent.extras?.getString("phone_number"))
            clipboardManager.setPrimaryClip(clip)
            Toast.makeText(context, "Broj kopiran!", Toast.LENGTH_LONG).show()
        }
    }
}