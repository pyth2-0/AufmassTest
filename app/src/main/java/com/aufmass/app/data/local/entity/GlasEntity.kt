package com.aufmass.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "glas",
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
data class GlasEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val aufmassId: Long,
    val bezeichnung: String,
    val glasart: String = "",
    val anzahl: Int = 1,
    val breite: Double = 0.0,
    val hoehe: Double = 0.0,
    val notizen: String = "",
    val fotoPath: String = ""
) {
    val flaeche: Double
        get() = breite * hoehe * anzahl
}
