package com.aufmass.app.data.local.dao

import androidx.room.*
import com.aufmass.app.data.local.entity.BodenSeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BodenSeDao {
    @Query("SELECT * FROM boden_se WHERE aufmassId = :aufmassId")
    fun getBodenSeByAufmassId(aufmassId: Long): Flow<List<BodenSeEntity>>

    @Query("SELECT * FROM boden_se WHERE id = :id")
    suspend fun getBodenSeById(id: Long): BodenSeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bodenSe: BodenSeEntity): Long

    @Update
    suspend fun update(bodenSe: BodenSeEntity)

    @Delete
    suspend fun delete(bodenSe: BodenSeEntity)

    @Query("DELETE FROM boden_se WHERE id = :id")
    suspend fun deleteById(id: Long)
}
