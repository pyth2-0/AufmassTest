package com.aufmass.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "boden_se",
    foreignKeys = [
        ForeignKey(
            entity = AufmassEntity::class,
            parentColumns = ["id"],
            childColumns = ["aufmassId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("aufmassId")]
)
data class BodenSeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val aufmassId: Long,
    val bezeichnung: String,
    val bodenart: String = "",
    val anzahl: Int = 1,
    val laenge: Double = 0.0,
    val breite: Double = 0.0,
    val notizen: String = "",
    val fotoPath: String = ""
) {
    val flaeche: Double
        get() = laenge * breite * anzahl
}
