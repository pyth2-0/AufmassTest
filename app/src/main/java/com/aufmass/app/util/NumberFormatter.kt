package com.aufmass.app.util

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object NumberFormatter {
    fun formatDecimal(value: Double, decimals: Int = 1): String {
        val pattern = if (decimals > 0) "0.${"0".repeat(decimals)}" else "0"
        return DecimalFormat(pattern, DecimalFormatSymbols.getInstance(Locale.GERMAN)).format(value)
    }
    
    fun formatDecimalWithUnit(value: Double, unit: String, decimals: Int = 1): String {
        return "${formatDecimal(value, decimals)}$unit"
    }
    
    fun parseDecimal(value: String): Double? {
        if (value.isBlank()) return null
        return value.replace(",", ".").toDoubleOrNull()
    }
    
    fun formatFlaeche(flaeche: Double): String = formatDecimal(flaeche) + "m²/h"
    
    fun formatMasse(laenge: Double, breite: Double): String = "${formatDecimal(laenge)}×${formatDecimal(breite)}m"
}
