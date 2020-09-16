package com.barisic.covid_19manager.util


import android.content.Context
import net.grandcentrix.tray.AppPreferences
import timber.log.Timber


class SharedPrefs(private val context: Context) {

    private var instance: AppPreferences? = null
    private fun getPrefs(): AppPreferences {
        if (instance == null) {
            instance = AppPreferences(context)
        }
        return instance!!
    }

    fun save(KEY_NAME: String, text: String) {
        Timber.tag(SHARED_PREFS).d("SAVE_STRING: $text")
        getPrefs().put(KEY_NAME, text)
    }

    fun save(KEY_NAME: String, status: Boolean) {
        Timber.tag(SHARED_PREFS).d("SAVE_BOOL $KEY_NAME: $status")
        getPrefs().put(KEY_NAME, status)
    }

    fun getValueString(keyName: String): String? {
        Timber.tag(SHARED_PREFS).d("get_STRING: ${getPrefs().getString(keyName, null)}")
        return getPrefs().getString(keyName, null)
    }

    fun getValueBoolean(KEY_NAME: String, defaultValue: Boolean): Boolean {
        Timber.tag(SHARED_PREFS)
            .d("GET_BOOL $KEY_NAME: ${getPrefs().getBoolean(KEY_NAME, defaultValue)}")
        return getPrefs().getBoolean(KEY_NAME, defaultValue)
    }

    fun clearSharedPreference() {
        getPrefs().clear()
    }
}