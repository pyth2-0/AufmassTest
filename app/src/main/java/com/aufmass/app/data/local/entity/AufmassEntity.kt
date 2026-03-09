package com.aufmass.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "aufmass")
data class AufmassEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val titel: String,
    val firma: String = "",
    val anschrift: String = "",
    val objektanschrift: String = "",
    val standardrhythmus: String = "1/Woche",
    val wochentage: String = "",
    val reinigungszeiten: String = "",
    val reinigungstage: String = "", // Mo,Di,Mi,Do,Fr,Sa,So
    val erstelltAm: Long = System.currentTimeMillis(),
    val notizen: String = ""
)
