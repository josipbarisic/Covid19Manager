package com.barisic.covid_19manager.receivers

import android.content.*
import android.widget.Toast
import com.barisic.covid_19manager.util.ACTION_COPY_PHONE_NUMBER


class EpidemiologistPhoneNumberReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null && intent.action == ACTION_COPY_PHONE_NUMBER) {
            val clipboardManager =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip =
                ClipData.newPlainText("Broj epidemiologa", intent.extras?.getString("phone_number"))
            clipboardManager.setPrimaryClip(clip)
            Toast.makeText(context, "Broj kopiran!", Toast.LENGTH_LONG).show()
        }
    }
}