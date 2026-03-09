package com.aufmass.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "einstellung_rhythmus")
data class RhythmusEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val klartext: String,
    val exportwert: Double, // Wert für Aufmaß-Export (z.B. 4.33 für 1/Woche)
    val lvWert: Double = 1.0 // Wert für LV (1 = 1x pro Rhythmus)
)
