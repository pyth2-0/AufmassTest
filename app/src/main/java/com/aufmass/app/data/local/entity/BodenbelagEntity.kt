package com.aufmass.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "einstellung_bodenbelag")
data class BodenbelagEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val bezeichnung: String,
    val abkuerzung: String,
    val quadratmeterSchnitt: Double = 0.0
)
