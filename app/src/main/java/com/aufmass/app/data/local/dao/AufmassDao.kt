package com.aufmass.app.data.local.dao

import androidx.room.*
import com.aufmass.app.data.local.entity.AufmassEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AufmassDao {
    @Query("SELECT * FROM aufmass ORDER BY erstelltAm DESC")
    fun getAllAufmass(): Flow<List<AufmassEntity>>

    @Query("SELECT * FROM aufmass WHERE id = :id")
    suspend fun getAufmassById(id: Long): AufmassEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(aufmass: AufmassEntity): Long

    @Update
    suspend fun update(aufmass: AufmassEntity)

    @Delete
    suspend fun delete(aufmass: AufmassEntity)

    @Query("DELETE FROM aufmass WHERE id = :id")
    suspend fun deleteById(id: Long)
}
