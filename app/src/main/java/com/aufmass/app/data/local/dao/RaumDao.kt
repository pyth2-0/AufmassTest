package com.aufmass.app.data.local.dao

import androidx.room.*
import com.aufmass.app.data.local.entity.RaumEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RaumDao {
    @Query("SELECT * FROM raum WHERE aufmassId = :aufmassId")
    fun getRaeumeByAufmassId(aufmassId: Long): Flow<List<RaumEntity>>

    @Query("SELECT * FROM raum WHERE id = :id")
    suspend fun getRaumById(id: Long): RaumEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(raum: RaumEntity): Long

    @Update
    suspend fun update(raum: RaumEntity)

    @Delete
    suspend fun delete(raum: RaumEntity)

    @Query("DELETE FROM raum WHERE id = :id")
    suspend fun deleteById(id: Long)
}
