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
        private const val KEY_RECONNECT_INTERVAL = "reconnect_interval"
        private const val KEY_LAST_CONNECTION_ATTEMPT = "last_connection_attempt"
        private const val KEY_BLUETOOTH_ENABLED = "bluetooth_enabled"

        @Volatile
        private var instance: BluetoothSettingsManager? = null

        fun getInstance(context: Context): BluetoothSettingsManager {
            return instance ?: synchronized(this) {
                instance ?: BluetoothSettingsManager(context.applicationContext).also { instance = it }
            }
        }
    }

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun reloadFromDisk() {
        prefs.edit().putString("__reload__", System.currentTimeMillis().toString()).commit()
    }

    var macAddress: String
        get() = prefs.getString(KEY_MAC_ADDRESS, "") ?: ""
        set(value) { prefs.edit().putString(KEY_MAC_ADDRESS, value).commit() }

    var autoJump: Boolean
        get() = prefs.getBoolean(KEY_AUTO_JUMP, true)
        set(value) { prefs.edit().putBoolean(KEY_AUTO_JUMP, value).commit() }

    var autoConnect: Boolean
        get() = prefs.getBoolean(KEY_AUTO_CONNECT, false)
        set(value) { prefs.edit().putBoolean(KEY_AUTO_CONNECT, value).commit() }

    var bluetoothEnabled: Boolean
        get() = prefs.getBoolean(KEY_BLUETOOTH_ENABLED, true)
        set(value) { prefs.edit().putBoolean(KEY_BLUETOOTH_ENABLED, value).commit() }

    var reconnectIntervalMs: Long
        get() = prefs.getLong(KEY_RECONNECT_INTERVAL, 30000L)
        set(value) { prefs.edit().putLong(KEY_RECONNECT_INTERVAL, value).commit() }

    var lastConnectionAttempt: Long
        get() = prefs.getLong(KEY_LAST_CONNECTION_ATTEMPT, 0L)
        set(value) { prefs.edit().putLong(KEY_LAST_CONNECTION_ATTEMPT, value).commit() }

    var lastDeviceName: String
        get() = prefs.getString(KEY_LAST_DEVICE_NAME, "") ?: ""
        set(value) { prefs.edit().putString(KEY_LAST_DEVICE_NAME, value).commit() }

    fun isConfigured(): Boolean = macAddress.isNotBlank()
}
