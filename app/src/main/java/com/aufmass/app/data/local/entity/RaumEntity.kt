package com.aufmass.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.aufmass.app.util.NumberFormatter

@Entity(
    tableName = "raum",
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
data class RaumEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val aufmassId: Long,
    val name: String,
    val raumart: String,
    val bodenbelag: String = "",
    val anzahl: Int = 1,
    val laenge: Double = 0.0,
    val breite: Double = 0.0,
    val schnittvorgabe: Double = 0.0,
    val rhythmus: String = "",
    val notizen: String = "",
    val fotoPaths: String = "",
    val zusatzlicheMasse: String = ""
) {
    val flaeche: Double
        get() = laenge * breite * anzahl
    
    val alleFlaechen: List<Double>
        get() {
            val result = mutableListOf(flaeche)
            if (zusatzlicheMasse.isNotBlank()) {
                zusatzlicheMasse.split(";").forEach { mass ->
                    val parts = mass.trim().split("x")
                    if (parts.size == 2) {
                        val l = NumberFormatter.parseDecimal(parts[0]) ?: 0.0
                        val b = NumberFormatter.parseDecimal(parts[1]) ?: 0.0
                        if (l > 0 && b > 0) {
                            result.add(l * b * anzahl)
                        }
                    }
                }
            }
            return result
        }
    
    val gesamtflaeche: Double
        get() = alleFlaechen.sum()
}
