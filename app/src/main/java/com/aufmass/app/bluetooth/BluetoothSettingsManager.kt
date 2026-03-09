package com.aufmass.app.bluetooth

import android.content.Context
import android.content.SharedPreferences

class BluetoothSettingsManager(context: Context) {

    companion object {
        private const val PREFS_NAME = "bluetooth_settings"
        private const val KEY_MAC_ADDRESS = "mac_address"
        private const val KEY_AUTO_JUMP = "auto_jump"
        private const val KEY_AUTO_CONNECT = "auto_connect"
        private const val KEY_LAST_DEVICE_NAME = "last_device_name"

        @Volatile
        private var instance: BluetoothSettingsManager? = null

        fun getInstance(context: Context): BluetoothSettingsManager {
            return instance ?: synchronized(this) {
                instance ?: BluetoothSettingsManager(context.applicationContext).also { instance = it }
            }
        }
    }

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var macAddress: String
        get() = prefs.getString(KEY_MAC_ADDRESS, "") ?: ""
        set(value) = prefs.edit().putString(KEY_MAC_ADDRESS, value).apply()

    var autoJump: Boolean
        get() = prefs.getBoolean(KEY_AUTO_JUMP, true)
        set(value) = prefs.edit().putBoolean(KEY_AUTO_JUMP, value).apply()

    var autoConnect: Boolean
        get() = prefs.getBoolean(KEY_AUTO_CONNECT, false)
        set(value) = prefs.edit().putBoolean(KEY_AUTO_CONNECT, value).apply()

    var lastDeviceName: String
        get() = prefs.getString(KEY_LAST_DEVICE_NAME, "") ?: ""
        set(value) = prefs.edit().putString(KEY_LAST_DEVICE_NAME, value).apply()

    fun isConfigured(): Boolean = macAddress.isNotBlank()
}
