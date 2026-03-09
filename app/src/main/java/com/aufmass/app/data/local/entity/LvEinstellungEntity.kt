package com.aufmass.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "einstellung_lv")
data class LvEinstellungEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val raumart: String,
    val spalte: String,
    val aufgabe: String,
    val rhythmusPlatzhalter: String = "{Rhythmus}"
)
