package com.aufmass.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "einstellung_raumart")
data class RaumartEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val bezeichnung: String,
    val schnittvorgabe: Double = 0.0
)
