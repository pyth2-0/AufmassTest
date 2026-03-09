package com.aufmass.app.bluetooth

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class BluetoothMeasurementHandler(context: Context) {

    companion object {
        @Volatile
        private var instance: BluetoothMeasurementHandler? = null

        fun getInstance(context: Context): BluetoothMeasurementHandler {
            return instance ?: synchronized(this) {
                instance ?: BluetoothMeasurementHandler(context.applicationContext).also { instance = it }
            }
        }
    }

    private val settingsManager = BluetoothSettingsManager.getInstance(context)
    private val bluetoothManager = BluetoothManager.getInstance(context)

    private val _currentField = MutableLiveData<MeasurementField>(MeasurementField.LENGTH)
    val currentField: LiveData<MeasurementField> = _currentField

    private val _pendingValue = MutableLiveData<Double?>()
    val pendingValue: LiveData<Double?> = _pendingValue

    private var lastMeasurementValue: Double? = null

    enum class MeasurementField {
        LENGTH,      // Länge
        WIDTH,       // Breite
        HEIGHT,      // Höhe (for Glas)
        QUANTITY,    // Menge
        NONE         // No pending field
    }

    fun resetField() {
        _currentField.value = MeasurementField.LENGTH
        _pendingValue.value = null
        lastMeasurementValue = null
    }

    fun onMeasurementReceived(value: Double): MeasurementField {
        if (!settingsManager.autoJump) {
            return MeasurementField.NONE
        }

        val currentValue = _currentField.value ?: MeasurementField.LENGTH
        val prevValue = lastMeasurementValue

        // Check if this is the same measurement (user pressed button again)
        val isRepeated = prevValue != null && kotlin.math.abs(prevValue - value) < 0.001

        lastMeasurementValue = value
        _pendingValue.value = value

        return if (isRepeated) {
            // Move to next field
            val nextField = when (currentValue) {
                MeasurementField.LENGTH -> MeasurementField.WIDTH
                MeasurementField.WIDTH -> MeasurementField.HEIGHT
                MeasurementField.HEIGHT -> MeasurementField.QUANTITY
                MeasurementField.QUANTITY -> MeasurementField.LENGTH
                MeasurementField.NONE -> MeasurementField.LENGTH
            }
            _currentField.value = nextField
            nextField
        } else {
            // First measurement - fill length
            _currentField.value = MeasurementField.LENGTH
            MeasurementField.LENGTH
        }
    }

    fun isAutoJumpEnabled(): Boolean = settingsManager.autoJump

    fun getCurrentField(): MeasurementField = _currentField.value ?: MeasurementField.LENGTH
}
