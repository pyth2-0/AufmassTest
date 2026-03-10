package com.aufmass.app.bluetooth

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class BluetoothManager(context: Context) {

    companion object {
        @Volatile
        private var instance: BluetoothManager? = null

        fun getInstance(context: Context): BluetoothManager {
            return instance ?: synchronized(this) {
                instance ?: BluetoothManager(context.applicationContext).also { instance = it }
            }
        }
    }

    private val settingsManager = BluetoothSettingsManager.getInstance(context)
    private val distoService = DistoBleService(context)
    private val handler = Handler(Looper.getMainLooper())
    private var reconnectRunnable: Runnable? = null

    private val _connectionState = MutableLiveData<ConnectionState>(ConnectionState.Disconnected)
    val connectionState: LiveData<ConnectionState> = _connectionState

    private val _lastMeasurement = MutableLiveData<DistoDataParser.DistoMeasurement?>()
    val lastMeasurement: LiveData<DistoDataParser.DistoMeasurement?> = _lastMeasurement

    private val _measurementCount = MutableLiveData(0)
    val measurementCount: LiveData<Int> = _measurementCount

    private val _logMessages = MutableLiveData<List<String>>(emptyList())
    val logMessages: LiveData<List<String>> = _logMessages

    private val _isScanning = MutableLiveData(false)
    val isScanning: LiveData<Boolean> = _isScanning

    enum class ConnectionState {
        Disconnected,
        Connecting,
        Connected,
        Error
    }

    init {
        distoService.setListener(object : DistoBleService.DistoListener {
            override fun onDiscoveryStarted() {
                _isScanning.postValue(true)
                addLog("Scan gestartet")
            }

            override fun onDiscoveryStopped() {
                _isScanning.postValue(false)
                addLog("Scan beendet")
            }

            override fun onDeviceFound(deviceName: String, deviceAddress: String, isDisto: Boolean) {
                addLog("Gerät gefunden: $deviceName ($deviceAddress)")
            }

            override fun onConnecting(deviceName: String) {
                _connectionState.postValue(ConnectionState.Connecting)
                settingsManager.lastConnectionAttempt = System.currentTimeMillis()
                addLog("Verbinde mit $deviceName...")
            }

            override fun onConnected(deviceName: String) {
                _connectionState.postValue(ConnectionState.Connected)
                settingsManager.lastDeviceName = deviceName
                settingsManager.lastConnectionAttempt = 0
                stopAutoReconnect()
                addLog("Verbunden mit $deviceName")
            }

            override fun onDisconnected() {
                _connectionState.postValue(ConnectionState.Disconnected)
                addLog("Getrennt")
                if (settingsManager.autoConnect && settingsManager.bluetoothEnabled) {
                    scheduleReconnect()
                }
            }

            override fun onMeasurementReceived(measurement: DistoDataParser.DistoMeasurement) {
                _measurementCount.postValue((_measurementCount.value ?: 0) + 1)
                _lastMeasurement.postValue(measurement)
                addLog("Messung: ${measurement.value} m")
            }

            override fun onError(message: String) {
                _connectionState.postValue(ConnectionState.Error)
                addLog("Fehler: $message")
                if (settingsManager.autoConnect && settingsManager.bluetoothEnabled) {
                    scheduleReconnect()
                }
            }

            override fun onLog(message: String) {
                addLog(message)
            }
        })
    }

    private fun addLog(message: String) {
        val currentLogs = _logMessages.value ?: emptyList()
        val timestampedMessage = "${System.currentTimeMillis() % 100000}: $message"
        _logMessages.postValue((currentLogs + timestampedMessage).takeLast(100))
    }

    fun isBluetoothAvailable(): Boolean = distoService.isBluetoothAvailable()
    fun isBluetoothEnabled(): Boolean = distoService.isBluetoothEnabled()
    fun isConnected(): Boolean = _connectionState.value == ConnectionState.Connected

    fun startScan() {
        if (!settingsManager.bluetoothEnabled) {
            addLog("Bluetooth ist deaktiviert")
            return
        }
        if (!isBluetoothEnabled()) {
            addLog("Bluetooth ist nicht eingeschaltet")
            return
        }
        _measurementCount.postValue(0)
        distoService.startScan()
    }

    fun stopScan() {
        distoService.stopScan()
    }

    fun connectToDevice(address: String) {
        if (!settingsManager.bluetoothEnabled) {
            addLog("Bluetooth ist deaktiviert")
            return
        }
        settingsManager.macAddress = address
        settingsManager.lastConnectionAttempt = System.currentTimeMillis()
        _measurementCount.postValue(0)
        distoService.connectByAddress(address)
    }

    fun connectToSavedDevice() {
        val address = settingsManager.macAddress
        if (address.isNotBlank()) {
            connectToDevice(address)
        } else {
            addLog("Keine MAC-Adresse gespeichert")
        }
    }

    fun disconnect() {
        stopAutoReconnect()
        distoService.disconnect()
    }

    fun startAutoReconnect() {
        if (!settingsManager.autoConnect || !settingsManager.bluetoothEnabled) {
            return
        }
        scheduleReconnect()
    }

    fun stopAutoReconnect() {
        reconnectRunnable?.let { handler.removeCallbacks(it) }
        reconnectRunnable = null
    }

    private fun scheduleReconnect() {
        if (!settingsManager.autoConnect || !settingsManager.bluetoothEnabled) {
            return
        }
        if (!isBluetoothEnabled()) {
            addLog("Bluetooth aus - kein Reconnect")
            return
        }

        reconnectRunnable?.let { handler.removeCallbacks(it) }

        val interval = settingsManager.reconnectIntervalMs
        val lastAttempt = settingsManager.lastConnectionAttempt
        val now = System.currentTimeMillis()

        // If we're not connected, try immediately if enough time has passed
        if (now - lastAttempt >= interval || lastAttempt == 0L) {
            addLog("Auto-Reconnect wird gestartet...")
            reconnectRunnable = Runnable {
                if (!isConnected() && settingsManager.bluetoothEnabled) {
                    connectToSavedDevice()
                }
            }
            handler.postDelayed(reconnectRunnable!!, 1000) // Small delay before attempting
        } else {
            // Schedule for later
            val delay = interval - (now - lastAttempt)
            addLog("Nächster Reconnect in ${delay/1000}s...")
            reconnectRunnable = Runnable {
                if (!isConnected() && settingsManager.bluetoothEnabled) {
                    connectToSavedDevice()
                }
            }
            handler.postDelayed(reconnectRunnable!!, delay)
        }
    }

    fun startPeriodicReconnect() {
        if (!settingsManager.autoConnect || !settingsManager.bluetoothEnabled) {
            return
        }
        
        reconnectRunnable?.let { handler.removeCallbacks(it) }
        
        reconnectRunnable = object : Runnable {
            override fun run() {
                if (!isConnected() && settingsManager.bluetoothEnabled && settingsManager.autoConnect) {
                    addLog("Periodischer Reconnect...")
                    connectToSavedDevice()
                }
                handler.postDelayed(this, settingsManager.reconnectIntervalMs)
            }
        }
        handler.postDelayed(reconnectRunnable!!, settingsManager.reconnectIntervalMs)
    }

    fun clearMeasurementCount() {
        _measurementCount.postValue(0)
    }

    fun clearLogs() {
        _logMessages.postValue(emptyList())
    }

    fun isBluetoothFeatureEnabled(): Boolean = settingsManager.bluetoothEnabled
}
