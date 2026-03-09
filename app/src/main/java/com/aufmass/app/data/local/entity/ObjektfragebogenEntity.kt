package com.aufmass.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "objektfragebogen",
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
data class ObjektfragebogenEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val aufmassId: Long,
    val materialkammer: String = "", // "", "vorhanden", "jede Etage"
    val waschmaschine: Boolean = false,
    val schmutzfangzone: Boolean = false,
    val wasser: Boolean = false,
    val strom: Boolean = false,
    val muelltrennung: Boolean = false,
    val muellentsorgung: String = "",
    val aufzug: Boolean = false,
    val reinigungszustand: String = "",
    val wechselgruende: String = "",
    val schluesselobjekt: Boolean = false,
    val alarmanlage: Boolean = false,
    val besonderheiten: String = ""
)
