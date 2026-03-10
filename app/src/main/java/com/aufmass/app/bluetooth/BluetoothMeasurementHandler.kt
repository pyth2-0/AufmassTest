package com.aufmass.app.bluetooth

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlin.math.abs

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

    private val _currentField = MutableLiveData<MeasurementField>(MeasurementField.NONE)
    val currentField: LiveData<MeasurementField> = _currentField

    private val _pendingValue = MutableLiveData<Double?>()
    val pendingValue: LiveData<Double?> = _pendingValue

    private val _lastReceivedValue = MutableLiveData<Double?>()
    val lastReceivedValue: LiveData<Double?> = _lastReceivedValue

    private var lastProcessedValue: Double? = null
    private var focusedField: MeasurementField = MeasurementField.NONE

    enum class MeasurementField {
        LENGTH,      // Länge (Räume)
        WIDTH,       // Breite (Räume/Glas)
        HEIGHT,      // Höhe (Glas)
        QUANTITY,    // Menge
        NONE         // Kein Feld ausgewählt
    }

    fun reset() {
        _currentField.value = MeasurementField.NONE
        _pendingValue.value = null
        _lastReceivedValue.value = null
        lastProcessedValue = null
        focusedField = MeasurementField.NONE
    }

    fun setFocusedField(field: MeasurementField) {
        focusedField = field
        if (field != MeasurementField.NONE) {
            _currentField.value = field
        }
    }

    fun clearFocusedField() {
        focusedField = MeasurementField.NONE
        _currentField.value = MeasurementField.NONE
    }

    fun isFieldFocused(): Boolean = focusedField != MeasurementField.NONE

    /**
     * Verarbeitet einen empfangenen Messwert
     * @return Das Feld in das der Wert eingetragen werden soll, oder NONE wenn nur angezeigt werden soll
     */
    fun onMeasurementReceived(value: Double): MeasurementResult {
        // Aktuellen Wert speichern für Anzeige
        _lastReceivedValue.value = value

        // Wenn Bluetooth deaktiviert - ignorieren
        if (!settingsManager.bluetoothEnabled) {
            return MeasurementResult.Ignored
        }

        // Wenn Bluetooth nicht verbunden - ignorieren
        if (!bluetoothManager.isConnected()) {
            return MeasurementResult.Ignored
        }

        // Wenn kein Feld fokussiert - nur anzeigen, nicht eintragen
        if (focusedField == MeasurementField.NONE) {
            return MeasurementResult.ShowOnly
        }

        // Wert runden auf 3 Kommastellen
        val roundedValue = (value * 1000).toLong() / 1000.0

        // Prüfen ob es ein wiederholter Wert ist (gleicher wie der letzte verarbeitete)
        val isRepeated = lastProcessedValue != null && abs(lastProcessedValue!! - roundedValue) < 0.0001

        return if (isRepeated) {
            // Wert ist identisch mit letztem - soll Springen auslösen
            val currentField = focusedField
            val nextField = getNextField(currentField)

            if (nextField != MeasurementField.NONE) {
                // Es gibt ein nächstes Feld - dahin springen
                _currentField.value = nextField
                focusedField = nextField
                lastProcessedValue = roundedValue
                _pendingValue.value = roundedValue
                MeasurementResult.JumpToField(nextField, roundedValue)
            } else {
                // Kein weiteres Feld - Wert bleibt stehen, kein Focus mehr
                lastProcessedValue = null // Reset für nächsten Durchgang
                focusedField = MeasurementField.NONE
                _currentField.value = MeasurementField.NONE
                _pendingValue.value = roundedValue
                MeasurementResult.CloseKeyboard
            }
        } else {
            // Erster Wert für dieses Feld - eintragen
            lastProcessedValue = roundedValue
            _pendingValue.value = roundedValue
            MeasurementResult.InsertValue(roundedValue)
        }
    }

    private fun getNextField(currentField: MeasurementField): MeasurementField {
        return when (currentField) {
            MeasurementField.LENGTH -> MeasurementField.WIDTH
            MeasurementField.WIDTH -> MeasurementField.HEIGHT
            MeasurementField.HEIGHT -> MeasurementField.QUANTITY
            MeasurementField.QUANTITY -> MeasurementField.NONE
            MeasurementField.NONE -> MeasurementField.NONE
        }
    }

    fun isAutoJumpEnabled(): Boolean = settingsManager.autoJump

    fun getCurrentField(): MeasurementField = _currentField.value ?: MeasurementField.NONE

    fun formatMeasurement(value: Double): String {
        // Auf 3 Kommastellen formatieren
        return String.format("%.3f", value).replace(".", ",")
    }

    sealed class MeasurementResult {
        object Ignored : MeasurementResult() // Keine Aktion
        object ShowOnly : MeasurementResult() // Nur anzeigen, nicht eintragen
        data class InsertValue(val value: Double) : MeasurementResult() // Wert eintragen
        data class JumpToField(val field: MeasurementField, val value: Double) : MeasurementResult() // Wert eintragen und springen
        object CloseKeyboard : MeasurementResult() // Wert bleibt, Tastatur schließen, Focus aufheben
    }
}
