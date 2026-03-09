package com.aufmass.app.data.local.dao

import androidx.room.*
import com.aufmass.app.data.local.entity.BodenbelagEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BodenbelagDao {
    @Query("SELECT * FROM einstellung_bodenbelag ORDER BY bezeichnung")
    fun getAllBodenbelage(): Flow<List<BodenbelagEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bodenbelag: BodenbelagEntity): Long

    @Update
    suspend fun update(bodenbelag: BodenbelagEntity)

    @Delete
    suspend fun delete(bodenbelag: BodenbelagEntity)

    @Query("DELETE FROM einstellung_bodenbelag")
    suspend fun deleteAll()
}
