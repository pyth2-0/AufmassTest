package com.aufmass.app.bluetooth

object DistoDataParser {

    data class DistoMeasurement(
        val value: Double,
        val unit: String,
        val rawData: String
    )

    fun parse(rawData: String): DistoMeasurement? {
        if (rawData.isBlank()) return null

        val trimmed = rawData.trim()
        val pattern = Regex("""([+-]?\d+\.?\d*)\s*(m|cm|mm|ft|in|')?""")
        val match = pattern.find(trimmed)

        return if (match != null) {
            val value = match.groupValues[1].toDoubleOrNull()
            val unit = match.groupValues[2].ifEmpty { "m" }

            if (value != null && value >= 0.01 && value < 100) {
                val convertedValue = convertToMeters(value, unit)
                DistoMeasurement(convertedValue, "m", trimmed)
            } else {
                null
            }
        } else {
            null
        }
    }

    fun parseBytes(bytes: ByteArray): DistoMeasurement? {
        if (bytes.isEmpty()) return null

        val ascii = String(bytes, Charsets.US_ASCII).trim()
        if (ascii.isNotBlank() && ascii.any { it.isDigit() } && ascii.length >= 3) {
            val parsed = parse(ascii)
            if (parsed != null) {
                return parsed
            }
        }

        val minValue = 0.01
        val maxValue = 50.0

        if (bytes.size >= 4) {
            try {
                val bits = bytes[0].toInt() and 0xFF or 
                          (bytes[1].toInt() and 0xFF shl 8) or
                          (bytes[2].toInt() and 0xFF shl 16) or 
                          (bytes[3].toInt() and 0xFF shl 24)
                val floatVal = java.lang.Float.intBitsToFloat(bits)
                if (!floatVal.isNaN() && !floatVal.isInfinite() && floatVal >= minValue && floatVal < maxValue) {
                    return DistoMeasurement(floatVal.toDouble(), "m", "$floatVal m")
                }
            } catch (e: Exception) { }
        }

        if (bytes.size >= 2) {
            try {
                val mm = bytes[0].toInt() and 0xFF or (bytes[1].toInt() and 0xFF shl 8)
                val meters = mm.toDouble() / 1000.0
                if (meters >= minValue && meters < maxValue) {
                    return DistoMeasurement(meters, "m", "$meters m")
                }
            } catch (e: Exception) { }
        }

        if (bytes.size >= 4) {
            try {
                val mm = bytes[0].toInt() and 0xFF or 
                        (bytes[1].toInt() and 0xFF shl 8) or
                        (bytes[2].toInt() and 0xFF shl 16) or 
                        (bytes[3].toInt() and 0xFF shl 24)
                val meters = mm.toDouble() / 1000.0
                if (meters >= minValue && meters < maxValue) {
                    return DistoMeasurement(meters, "m", "$meters m")
                }
            } catch (e: Exception) { }
        }

        return null
    }

    private fun convertToMeters(value: Double, unit: String): Double {
        return when (unit.lowercase()) {
            "m" -> value
            "cm" -> value / 100.0
            "mm" -> value / 1000.0
            "ft" -> value * 0.3048
            "in" -> value * 0.0254
            "'" -> value * 0.3048
            else -> value
        }
    }
}
