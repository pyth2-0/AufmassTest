package com.aufmass.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "einstellung_glasart")
data class GlasartEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val bezeichnung: String
)
