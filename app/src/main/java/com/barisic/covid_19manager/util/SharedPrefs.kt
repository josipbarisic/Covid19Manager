package com.barisic.covid_19manager.util


import android.content.Context
import android.util.Log
import net.grandcentrix.tray.AppPreferences


class SharedPrefs(private val context: Context) {

    private var instance: AppPreferences? = null
    private fun getPrefs(): AppPreferences {
        if (instance == null) {
            instance = AppPreferences(context)
        }
        return instance!!
    }

    fun save(KEY_NAME: String, text: String) {
        Log.d("SHARED_PREFS", "SAVE_STRING: $text")
        getPrefs().put(KEY_NAME, text)
    }

    fun save(KEY_NAME: String, value: Int) {
        getPrefs().put(KEY_NAME, value)
    }

    fun save(KEY_NAME: String, status: Boolean) {
        Log.d("SHARED_PREFS", "SAVE_BOOL $KEY_NAME: $status")
        getPrefs().put(KEY_NAME, status)
    }

    fun getValueString(keyName: String): String? {
        Log.d("SHARED_PREFS", "get_STRING: ${getPrefs().getString(keyName, null)}")
        return getPrefs().getString(keyName, null)
    }

    fun getValueInt(KEY_NAME: String): Int {
        return getPrefs().getInt(KEY_NAME, 0)
    }

    fun getValueBoolean(KEY_NAME: String, defaultValue: Boolean): Boolean {
        Log.d(
            "SHARED_PREFS",
            "GET_BOOL $KEY_NAME: ${getPrefs().getBoolean(KEY_NAME, defaultValue)}"
        )
        return getPrefs().getBoolean(KEY_NAME, defaultValue)
    }

    fun clearSharedPreference() {
        getPrefs().clear()
    }

    fun removeValue(KEY_NAME: String) {
        Log.d("SHARED_PREFS", "removeValue: $KEY_NAME")
        getPrefs().remove(KEY_NAME)
    }
}