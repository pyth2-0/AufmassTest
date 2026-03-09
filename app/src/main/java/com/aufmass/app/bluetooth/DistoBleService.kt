package com.aufmass.app.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log

class DistoBleService(private val context: Context) {

    companion object {
        const val DITO_SERVICE_UUID = "3ab10100-f831-4395-b29d-570977d5bf94"
        const val DISTO_MEASUREMENT_CHAR_UUID = "3ab1010c-f831-4395-b29d-570977d5bf94"
        const val DISTO_DATA_CHAR_UUID = "3ab10101-f831-4395-b29d-570977d5bf94"
        const val DISTO_DATA2_CHAR_UUID = "3ab10102-f831-4395-b29d-570977d5bf94"
        const val DISTO_CMD_CHAR_UUID = "3ab1010d-f831-4395-b29d-570977d5bf94"
        const val CLIENT_CONFIG_DESCRIPTOR_UUID = "00002902-0000-1000-8000-00805f9b34fb"
        private const val TAG = "DistoBLE"
    }

    interface DistoListener {
        fun onDiscoveryStarted()
        fun onDiscoveryStopped()
        fun onDeviceFound(deviceName: String, deviceAddress: String, isDisto: Boolean)
        fun onConnecting(deviceName: String)
        fun onConnected(deviceName: String)
        fun onDisconnected()
        fun onMeasurementReceived(measurement: DistoDataParser.DistoMeasurement)
        fun onError(message: String)
        fun onLog(message: String)
    }

    private var bluetoothAdapter: BluetoothAdapter? = null
    private var bluetoothGatt: BluetoothGatt? = null
    var listener: DistoListener? = null
        private set
    private val mainHandler = Handler(Looper.getMainLooper())
    private var pendingCharacteristics = mutableListOf<BluetoothGattCharacteristic>()

    init {
        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager
        bluetoothAdapter = bluetoothManager?.adapter
    }

    fun setListener(listener: DistoListener) {
        this.listener = listener
    }

    fun isBluetoothAvailable(): Boolean = bluetoothAdapter != null
    fun isBluetoothEnabled(): Boolean = bluetoothAdapter?.isEnabled == true

    @SuppressLint("MissingPermission")
    fun startScan() {
        if (!isBluetoothEnabled()) {
            listener?.onError("Bluetooth ist nicht eingeschaltet")
            return
        }

        listener?.onLog("Starte BLE-Scan...")
        listener?.onDiscoveryStarted()

        try {
            val success = bluetoothAdapter?.startLeScan(leScanCallback)
            if (success == true) {
                listener?.onLog("Scan gestartet")
                mainHandler.postDelayed({
                    stopScan()
                }, 15000)
            } else {
                listener?.onError("Scan fehlgeschlagen")
            }
        } catch (e: Exception) {
            listener?.onError("Scan Fehler: ${e.message}")
        }
    }

    private val leScanCallback = BluetoothAdapter.LeScanCallback { device, rssi, scanRecord ->
        val name = device?.name ?: "Unbekannt"
        val address = device?.address ?: ""
        
        listener?.onLog("Gerät: $name ($address)")
        
        val isDisto = name.contains("DISTO", ignoreCase = true)
        
        listener?.onDeviceFound(name, address, isDisto)
        
        if (isDisto) {
            listener?.onLog("Disto erkannt - verbinde...")
            stopScan()
            connectToDevice(device!!)
        }
    }

    @SuppressLint("MissingPermission")
    fun stopScan() {
        try {
            bluetoothAdapter?.stopLeScan(leScanCallback)
            listener?.onDiscoveryStopped()
        } catch (e: Exception) {
            listener?.onLog("Stop scan error: ${e.message}")
        }
    }

    @SuppressLint("MissingPermission")
    fun connectToDevice(device: BluetoothDevice) {
        listener?.onConnecting(device.name ?: device.address)
        listener?.onLog("Verbinde mit ${device.name}...")

        try {
            bluetoothGatt = device.connectGatt(context, false, gattCallback, BluetoothDevice.TRANSPORT_LE)
        } catch (e: Exception) {
            listener?.onError("Verbindungsfehler: ${e.message}")
        }
    }

    @SuppressLint("MissingPermission")
    fun connectByAddress(address: String) {
        try {
            val device = bluetoothAdapter?.getRemoteDevice(address)
            if (device != null) {
                connectToDevice(device)
            } else {
                listener?.onError("Gerät nicht gefunden: $address")
            }
        } catch (e: Exception) {
            listener?.onError("Fehler: ${e.message}")
        }
    }

    private val gattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> {
                    mainHandler.post {
                        listener?.onLog("Verbunden mit GATT")
                        listener?.onConnected(gatt.device.name ?: gatt.device.address)
                        gatt.discoverServices()
                    }
                }
                BluetoothProfile.STATE_DISCONNECTED -> {
                    mainHandler.post {
                        listener?.onLog("Getrennt (Status: $status)")
                        listener?.onDisconnected()
                    }
                }
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                mainHandler.post {
                    listener?.onLog("Services entdeckt - aktiviere Notifications...")
                    logAllServices(gatt)
                    enableAllNotifications(gatt)
                }
            } else {
                mainHandler.post {
                    listener?.onError("Services Fehler: $status")
                }
            }
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            value: ByteArray
        ) {
            val uuid = characteristic.uuid.toString()
            mainHandler.post {
                listener?.onLog("Daten von: $uuid")
                listener?.onLog("Bytes: ${value.joinToString(" ")}")
                
                val measurement = DistoDataParser.parseBytes(value)
                if (measurement != null) {
                    listener?.onLog("Messung: ${measurement.value} m")
                    listener?.onMeasurementReceived(measurement)
                } else {
                    val data = bytesToReadableString(value)
                    listener?.onLog("Unbekannt: $data")
                }
            }
        }

        override fun onDescriptorWrite(gatt: BluetoothGatt, descriptor: BluetoothGattDescriptor, status: Int) {
            mainHandler.post {
                listener?.onLog("Descriptor geschrieben: ${descriptor.characteristic.uuid}, Status: $status")
                
                if (pendingCharacteristics.isNotEmpty()) {
                    val char = pendingCharacteristics.removeAt(0)
                    writeNextDescriptor(gatt, char)
                } else {
                    listener?.onLog("Alle Notifications aktiviert!")
                }
            }
        }
    }

    private fun logAllServices(gatt: BluetoothGatt) {
        for (service in gatt.services) {
            listener?.onLog("Service: ${service.uuid}")
            for (char in service.characteristics) {
                val props = char.properties
                val notify = (props and BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0
                val indicate = (props and BluetoothGattCharacteristic.PROPERTY_INDICATE) != 0
                val read = (props and BluetoothGattCharacteristic.PROPERTY_READ) != 0
                val write = (props and BluetoothGattCharacteristic.PROPERTY_WRITE) != 0
                listener?.onLog("  Char: ${char.uuid} [R:$read W:$write N:$notify I:$indicate]")
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun enableAllNotifications(gatt: BluetoothGatt) {
        val service = gatt.getService(java.util.UUID.fromString(DITO_SERVICE_UUID))
        
        if (service == null) {
            listener?.onError("Disto Service nicht gefunden!")
            return
        }
        
        listener?.onLog("Disto Service gefunden: ${service.uuid}")
        
        pendingCharacteristics.clear()
        
        for (char in service.characteristics) {
            val props = char.properties
            val canNotify = (props and BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0
            val canIndicate = (props and BluetoothGattCharacteristic.PROPERTY_INDICATE) != 0
            
            if (canNotify || canIndicate) {
                listener?.onLog("Aktiviere: ${char.uuid}")
                pendingCharacteristics.add(char)
            }
        }
        
        if (pendingCharacteristics.isNotEmpty()) {
            val char = pendingCharacteristics.removeAt(0)
            writeNextDescriptor(gatt, char)
        }
    }

    @SuppressLint("MissingPermission")
    private fun writeNextDescriptor(gatt: BluetoothGatt, char: BluetoothGattCharacteristic) {
        try {
            gatt.setCharacteristicNotification(char, true)
            
            val descriptor = char.getDescriptor(java.util.UUID.fromString(CLIENT_CONFIG_DESCRIPTOR_UUID))
            if (descriptor != null) {
                try {
                    descriptor.value = BluetoothGattDescriptor.ENABLE_INDICATION_VALUE
                } catch (e: Exception) {
                    descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                }
                
                val success = gatt.writeDescriptor(descriptor)
                listener?.onLog("Descriptor geschrieben für ${char.uuid}: $success")
            } else {
                listener?.onLog("Kein Descriptor für ${char.uuid}")
                if (pendingCharacteristics.isNotEmpty()) {
                    val next = pendingCharacteristics.removeAt(0)
                    writeNextDescriptor(gatt, next)
                }
            }
        } catch (e: Exception) {
            listener?.onLog("Fehler: ${e.message}")
        }
    }

    private fun bytesToReadableString(bytes: ByteArray): String {
        val ascii = String(bytes, Charsets.US_ASCII).trim('\u0000', ' ')
        if (ascii.isNotBlank() && ascii.all { it.isLetterOrDigit() || it in "+-.,m" }) {
            return ascii
        }
        return bytes.joinToString("") { "%02x".format(it) }
    }

    @SuppressLint("MissingPermission")
    fun disconnect() {
        try {
            bluetoothGatt?.disconnect()
            bluetoothGatt?.close()
            bluetoothGatt = null
        } catch (e: Exception) {
            listener?.onLog("Disconnect error: ${e.message}")
        }
    }

    fun isConnected(): Boolean = bluetoothGatt != null
}
